package com.hotel.chatbot.service;

import com.hotel.chatbot.entity.Booking;
import com.hotel.chatbot.entity.Hotel;
import com.hotel.chatbot.entity.User;
import com.hotel.chatbot.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service for managing hotel bookings.
 * Handles booking creation, modification, and cancellation.
 */
@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AtomicLong dailyBookingCounter = new AtomicLong(0);
    private volatile LocalDate lastResetDate = LocalDate.now();

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        initializeCounter();
    }

    /**
     * Creates a new booking with auto-generated booking reference.
     *
     * @param hotel    the hotel to book
     * @param checkIn  check-in date
     * @param checkOut check-out date
     * @param guests   number of guests
     * @param user     optional user making the booking
     * @return the created booking
     */
    public Booking createBooking(Hotel hotel, LocalDate checkIn, LocalDate checkOut, 
                                  Integer guests, User user) {
        // Validate inputs
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel must not be null");
        }
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates must not be null");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        if (guests == null || guests < 1) {
            throw new IllegalArgumentException("Number of guests must be at least 1");
        }

        // Generate booking reference
        String bookingReference = generateBookingReference();

        // Create booking
        Booking booking = new Booking(bookingReference, hotel, checkIn, checkOut, guests);
        if (user != null) {
            booking.setUser(user);
        }

        return bookingRepository.save(booking);
    }

    /**
     * Generates a unique booking reference in the format HBK-YYYY-XXXXX.
     * Example: HBK-2026-00001
     *
     * @return the generated booking reference
     */
    private String generateBookingReference() {
        // Reset counter if it's a new day
        LocalDate today = LocalDate.now();
        if (!today.equals(lastResetDate)) {
            synchronized (this) {
                if (!today.equals(lastResetDate)) {
                    dailyBookingCounter.set(0);
                    lastResetDate = today;
                }
            }
        }

        int year = Year.now().getValue();
        long sequence = dailyBookingCounter.incrementAndGet();
        
        // Format: HBK-YYYY-XXXXX (5 digits, zero-padded)
        return String.format("HBK-%d-%05d", year, sequence);
    }

    /**
     * Initializes the booking counter based on existing bookings for today.
     */
    private void initializeCounter() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        long todayCount = bookingRepository.countBookingsCreatedToday(startOfDay);
        dailyBookingCounter.set(todayCount);
    }
}
