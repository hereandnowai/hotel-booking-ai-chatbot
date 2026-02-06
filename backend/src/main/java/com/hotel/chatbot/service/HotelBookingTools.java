package com.hotel.chatbot.service;

import com.hotel.chatbot.dto.BookingInfo;
import com.hotel.chatbot.dto.HotelSearchResult;
import com.hotel.chatbot.entity.Booking;
import com.hotel.chatbot.entity.BookingStatus;
import com.hotel.chatbot.entity.Hotel;
import com.hotel.chatbot.repository.BookingRepository;
import com.hotel.chatbot.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * AI Function Calling Tools for hotel booking operations.
 * These methods are exposed to the LLM for function calling.
 */
@Component
public class HotelBookingTools {

    private static final Logger log = LoggerFactory.getLogger(HotelBookingTools.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    public HotelBookingTools(HotelRepository hotelRepository, 
                              BookingRepository bookingRepository,
                              BookingService bookingService) {
        this.hotelRepository = hotelRepository;
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
    }

    /**
     * Searches for available hotels in a city.
     *
     * @param city the city to search in
     * @param maxPrice optional maximum price per night
     * @param roomType optional room type filter (single, double, suite)
     * @return formatted string with search results
     */
    @Tool(description = "Search for available hotels in a specific city. Returns a list of hotels with their details including name, price, and room type.")
    public String searchHotels(
            @ToolParam(description = "The city to search for hotels in (e.g., Chennai, Bangalore, Mumbai, Delhi, Goa)") String city,
            @ToolParam(description = "Optional maximum price per night in INR", required = false) Integer maxPrice,
            @ToolParam(description = "Optional room type filter: single, double, or suite", required = false) String roomType) {
        
        log.info("Searching hotels in {} with maxPrice={}, roomType={}", city, maxPrice, roomType);

        List<Hotel> hotels;
        
        if (roomType != null && !roomType.isBlank()) {
            hotels = hotelRepository.findAvailableByRoomType(city, roomType.trim());
        } else if (maxPrice != null) {
            hotels = hotelRepository.findAvailableByPriceRange(city, 0, maxPrice);
        } else {
            hotels = hotelRepository.findByCityContainingIgnoreCaseAndAvailabilityTrue(city);
        }

        if (hotels.isEmpty()) {
            return "No available hotels found in " + city + ". Please try a different city or adjust your search criteria.";
        }

        // Filter by max price if both roomType and maxPrice are specified
        if (maxPrice != null && roomType != null) {
            hotels = hotels.stream()
                    .filter(h -> h.getPricePerNight() <= maxPrice)
                    .toList();
        }

        var sb = new StringBuilder();
        sb.append("Found ").append(hotels.size()).append(" hotel(s) in ").append(city).append(":\n\n");

        for (int i = 0; i < hotels.size(); i++) {
            var hotel = hotels.get(i);
            sb.append(i + 1).append(". **").append(hotel.getName()).append("**\n");
            sb.append("   📍 ").append(hotel.getAddress() != null ? hotel.getAddress() : hotel.getCity()).append("\n");
            sb.append("   🛏️ ").append(hotel.getRoomType()).append(" Room\n");
            sb.append("   💰 ₹").append(String.format("%,d", hotel.getPricePerNight())).append(" per night\n");
            if (hotel.getRating() != null) {
                sb.append("   ⭐ ").append(hotel.getRating()).append("/5 rating\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Creates a new hotel booking.
     *
     * @param hotelName name of the hotel to book
     * @param city city of the hotel
     * @param checkInDate check-in date (YYYY-MM-DD format)
     * @param checkOutDate check-out date (YYYY-MM-DD format)
     * @param guests number of guests
     * @return booking confirmation or error message
     */
    @Tool(description = "Create a new hotel booking. Requires hotel name, city, check-in date, check-out date, and number of guests.")
    public String createBooking(
            @ToolParam(description = "Name of the hotel to book") String hotelName,
            @ToolParam(description = "City where the hotel is located") String city,
            @ToolParam(description = "Check-in date in YYYY-MM-DD format") String checkInDate,
            @ToolParam(description = "Check-out date in YYYY-MM-DD format") String checkOutDate,
            @ToolParam(description = "Number of guests") Integer guests) {
        
        log.info("Creating booking for {} in {}, dates: {} to {}, guests: {}", 
                hotelName, city, checkInDate, checkOutDate, guests);

        try {
            // Parse dates
            LocalDate checkIn = parseDate(checkInDate);
            LocalDate checkOut = parseDate(checkOutDate);

            // Validate dates
            if (checkIn.isBefore(LocalDate.now())) {
                return "❌ Check-in date cannot be in the past. Please provide a future date.";
            }
            if (!checkOut.isAfter(checkIn)) {
                return "❌ Check-out date must be after check-in date.";
            }
            if (guests < 1 || guests > 10) {
                return "❌ Number of guests must be between 1 and 10.";
            }

            // Find the hotel
            List<Hotel> matchingHotels = hotelRepository.searchHotels(hotelName);
            if (matchingHotels.isEmpty()) {
                return "❌ Could not find a hotel named '" + hotelName + "'. Please search for available hotels first.";
            }

            // Find best match in the specified city
            Optional<Hotel> hotelOpt = matchingHotels.stream()
                    .filter(h -> h.getCity().toLowerCase().contains(city.toLowerCase()))
                    .findFirst();

            if (hotelOpt.isEmpty()) {
                hotelOpt = Optional.of(matchingHotels.get(0));
            }

            Hotel hotel = hotelOpt.get();

            // Create the booking
            Booking booking = bookingService.createBooking(hotel, checkIn, checkOut, guests, null);

            return formatBookingConfirmation(booking, hotel);

        } catch (DateTimeParseException e) {
            return "❌ Invalid date format. Please use YYYY-MM-DD format (e.g., 2026-03-15).";
        } catch (Exception e) {
            log.error("Error creating booking", e);
            return "❌ An error occurred while creating the booking: " + e.getMessage();
        }
    }

    /**
     * Modifies an existing booking.
     *
     * @param bookingReference the booking reference ID (e.g., HBK-2026-00123)
     * @param newCheckInDate optional new check-in date
     * @param newCheckOutDate optional new check-out date
     * @param newGuests optional new number of guests
     * @return modification confirmation or error message
     */
    @Tool(description = "Modify an existing booking. Requires booking reference ID. Can update check-in date, check-out date, or number of guests.")
    public String modifyBooking(
            @ToolParam(description = "Booking reference ID (e.g., HBK-2026-00123)") String bookingReference,
            @ToolParam(description = "New check-in date in YYYY-MM-DD format", required = false) String newCheckInDate,
            @ToolParam(description = "New check-out date in YYYY-MM-DD format", required = false) String newCheckOutDate,
            @ToolParam(description = "New number of guests", required = false) Integer newGuests) {
        
        log.info("Modifying booking {}", bookingReference);

        Optional<Booking> bookingOpt = bookingRepository.findByBookingReference(bookingReference.toUpperCase());
        
        if (bookingOpt.isEmpty()) {
            return "❌ Booking not found with reference: " + bookingReference + 
                   ". Please verify the booking ID and try again.";
        }

        Booking booking = bookingOpt.get();

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return "❌ This booking has been cancelled and cannot be modified.";
        }

        try {
            // Apply modifications
            if (newCheckInDate != null && !newCheckInDate.isBlank()) {
                LocalDate checkIn = parseDate(newCheckInDate);
                if (checkIn.isBefore(LocalDate.now())) {
                    return "❌ New check-in date cannot be in the past.";
                }
                booking.setCheckIn(checkIn);
            }

            if (newCheckOutDate != null && !newCheckOutDate.isBlank()) {
                LocalDate checkOut = parseDate(newCheckOutDate);
                booking.setCheckOut(checkOut);
            }

            if (newGuests != null && newGuests > 0) {
                if (newGuests > 10) {
                    return "❌ Maximum 10 guests allowed per booking.";
                }
                booking.setGuests(newGuests);
            }

            booking.markAsModified();
            bookingRepository.save(booking);

            var sb = new StringBuilder();
            sb.append("✅ **Booking Modified Successfully!**\n\n");
            sb.append("📋 Booking ID: **").append(booking.getBookingReference()).append("**\n");
            sb.append("🏨 Hotel: ").append(booking.getHotel().getName()).append("\n");
            sb.append("📅 New Dates: ").append(booking.getCheckIn()).append(" to ").append(booking.getCheckOut()).append("\n");
            sb.append("👥 Guests: ").append(booking.getGuests()).append("\n");
            sb.append("💰 Updated Total: ₹").append(String.format("%,d", booking.getTotalPrice())).append("\n");
            sb.append("📌 Status: ").append(booking.getStatus()).append("\n");

            return sb.toString();

        } catch (DateTimeParseException e) {
            return "❌ Invalid date format. Please use YYYY-MM-DD format.";
        } catch (IllegalArgumentException e) {
            return "❌ " + e.getMessage();
        }
    }

    /**
     * Cancels an existing booking.
     *
     * @param bookingReference the booking reference ID
     * @return cancellation confirmation or error message
     */
    @Tool(description = "Cancel an existing hotel booking. Requires the booking reference ID.")
    public String cancelBooking(
            @ToolParam(description = "Booking reference ID to cancel (e.g., HBK-2026-00123)") String bookingReference) {
        
        log.info("Cancelling booking {}", bookingReference);

        Optional<Booking> bookingOpt = bookingRepository.findByBookingReference(bookingReference.toUpperCase());
        
        if (bookingOpt.isEmpty()) {
            return "❌ Booking not found with reference: " + bookingReference + 
                   ". Please verify the booking ID and try again.";
        }

        Booking booking = bookingOpt.get();

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return "ℹ️ This booking has already been cancelled.";
        }

        booking.cancel();
        bookingRepository.save(booking);

        var sb = new StringBuilder();
        sb.append("✅ **Booking Cancelled Successfully**\n\n");
        sb.append("📋 Booking ID: **").append(booking.getBookingReference()).append("**\n");
        sb.append("🏨 Hotel: ").append(booking.getHotel().getName()).append("\n");
        sb.append("📅 Original Dates: ").append(booking.getCheckIn()).append(" to ").append(booking.getCheckOut()).append("\n");
        sb.append("📌 Status: CANCELLED\n\n");
        sb.append("If you need to make a new booking, I'd be happy to help!");

        return sb.toString();
    }

    /**
     * Gets details of an existing booking.
     *
     * @param bookingReference the booking reference ID
     * @return booking details or error message
     */
    @Tool(description = "Get details of an existing booking using the booking reference ID.")
    public String getBookingDetails(
            @ToolParam(description = "Booking reference ID (e.g., HBK-2026-00123)") String bookingReference) {
        
        log.info("Getting booking details for {}", bookingReference);

        Optional<Booking> bookingOpt = bookingRepository.findByBookingReference(bookingReference.toUpperCase());
        
        if (bookingOpt.isEmpty()) {
            return "❌ Booking not found with reference: " + bookingReference;
        }

        Booking booking = bookingOpt.get();
        Hotel hotel = booking.getHotel();

        var sb = new StringBuilder();
        sb.append("📋 **Booking Details**\n\n");
        sb.append("🆔 Booking ID: **").append(booking.getBookingReference()).append("**\n");
        sb.append("📌 Status: ").append(booking.getStatus()).append("\n\n");
        sb.append("🏨 **Hotel Information**\n");
        sb.append("   Name: ").append(hotel.getName()).append("\n");
        sb.append("   Location: ").append(hotel.getCity());
        if (hotel.getAddress() != null) {
            sb.append(" - ").append(hotel.getAddress());
        }
        sb.append("\n   Room Type: ").append(hotel.getRoomType()).append("\n\n");
        sb.append("📅 **Stay Details**\n");
        sb.append("   Check-in: ").append(booking.getCheckIn()).append("\n");
        sb.append("   Check-out: ").append(booking.getCheckOut()).append("\n");
        sb.append("   Duration: ").append(booking.getNumberOfNights()).append(" night(s)\n");
        sb.append("   Guests: ").append(booking.getGuests()).append("\n\n");
        sb.append("💰 **Payment**\n");
        sb.append("   Total: ₹").append(String.format("%,d", booking.getTotalPrice())).append("\n");

        return sb.toString();
    }

    private LocalDate parseDate(String dateStr) {
        // Try multiple date formats
        String normalized = dateStr.trim();
        
        // Try ISO format first
        try {
            return LocalDate.parse(normalized, DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
            // Try other formats
        }

        // Try common formats
        String[] patterns = {"dd-MM-yyyy", "dd/MM/yyyy", "MM-dd-yyyy", "MM/dd/yyyy"};
        for (String pattern : patterns) {
            try {
                return LocalDate.parse(normalized, DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException ignored) {
                // Continue to next pattern
            }
        }

        throw new DateTimeParseException("Could not parse date", dateStr, 0);
    }

    private String formatBookingConfirmation(Booking booking, Hotel hotel) {
        var sb = new StringBuilder();
        sb.append("✅ **Booking Confirmed!**\n\n");
        sb.append("📋 Booking ID: **").append(booking.getBookingReference()).append("**\n\n");
        sb.append("🏨 **Hotel Details**\n");
        sb.append("   ").append(hotel.getName()).append("\n");
        sb.append("   📍 ").append(hotel.getCity());
        if (hotel.getAddress() != null) {
            sb.append(" - ").append(hotel.getAddress());
        }
        sb.append("\n   🛏️ ").append(hotel.getRoomType()).append(" Room\n\n");
        sb.append("📅 **Stay Details**\n");
        sb.append("   Check-in: ").append(booking.getCheckIn()).append("\n");
        sb.append("   Check-out: ").append(booking.getCheckOut()).append("\n");
        sb.append("   Duration: ").append(booking.getNumberOfNights()).append(" night(s)\n");
        sb.append("   Guests: ").append(booking.getGuests()).append("\n\n");
        sb.append("💰 **Total Amount: ₹").append(String.format("%,d", booking.getTotalPrice())).append("**\n\n");
        sb.append("Please save your booking ID for future reference. Is there anything else I can help you with?");

        return sb.toString();
    }
}
