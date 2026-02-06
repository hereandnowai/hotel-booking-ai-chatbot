package com.hotel.chatbot.dto;

import com.hotel.chatbot.entity.Booking;
import com.hotel.chatbot.entity.BookingStatus;

/**
 * DTO for booking information returned to the client.
 *
 * @param bookingId   the booking reference ID
 * @param hotelName   name of the hotel
 * @param city        city of the hotel
 * @param checkIn     check-in date (ISO format)
 * @param checkOut    check-out date (ISO format)
 * @param guests      number of guests
 * @param status      booking status
 * @param totalPrice  total price in INR
 */
public record BookingInfo(
        String bookingId,
        String hotelName,
        String city,
        String checkIn,
        String checkOut,
        Integer guests,
        BookingStatus status,
        Integer totalPrice
) {
    /**
     * Creates a BookingInfo from a Booking entity.
     */
    public static BookingInfo fromEntity(Booking booking) {
        return new BookingInfo(
                booking.getBookingReference(),
                booking.getHotel().getName(),
                booking.getHotel().getCity(),
                booking.getCheckIn().toString(),
                booking.getCheckOut().toString(),
                booking.getGuests(),
                booking.getStatus(),
                booking.getTotalPrice()
        );
    }
}
