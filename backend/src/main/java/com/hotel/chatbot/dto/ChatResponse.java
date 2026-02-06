package com.hotel.chatbot.dto;

import java.util.List;

/**
 * Response DTO for chat messages.
 *
 * @param message     the AI assistant's response message
 * @param bookingInfo optional booking information if a booking was created/modified
 * @param hotels      optional list of hotels from a search result
 */
public record ChatResponse(
        String message,
        BookingInfo bookingInfo,
        List<HotelSearchResult> hotels
) {
    /**
     * Creates a simple text response without booking info or hotels.
     */
    public static ChatResponse textOnly(String message) {
        return new ChatResponse(message, null, null);
    }

    /**
     * Creates a response with booking information.
     */
    public static ChatResponse withBooking(String message, BookingInfo bookingInfo) {
        return new ChatResponse(message, bookingInfo, null);
    }

    /**
     * Creates a response with hotel search results.
     */
    public static ChatResponse withHotels(String message, List<HotelSearchResult> hotels) {
        return new ChatResponse(message, null, hotels);
    }
}
