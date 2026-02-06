package com.hotel.chatbot.exception;

/**
 * Exception thrown when a booking is not found.
 */
public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(String bookingReference) {
        super("Booking not found with reference: " + bookingReference);
    }
}
