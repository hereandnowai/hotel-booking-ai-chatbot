package com.hotel.chatbot.entity;

/**
 * Enumeration representing the possible statuses of a booking.
 */
public enum BookingStatus {
    /**
     * The booking has been confirmed and is active.
     */
    CONFIRMED,

    /**
     * The booking has been cancelled by the user or system.
     */
    CANCELLED,

    /**
     * The booking has been modified from its original state.
     */
    MODIFIED
}
