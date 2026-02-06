package com.hotel.chatbot.dto;

import com.hotel.chatbot.entity.Hotel;

import java.math.BigDecimal;

/**
 * DTO for hotel search results.
 *
 * @param id            hotel ID
 * @param name          hotel name
 * @param city          city location
 * @param address       full address
 * @param pricePerNight price per night in INR
 * @param roomType      type of room (single, double, suite)
 * @param rating        hotel rating (1-5)
 * @param available     availability status
 */
public record HotelSearchResult(
        String id,
        String name,
        String city,
        String address,
        Integer pricePerNight,
        String roomType,
        BigDecimal rating,
        Boolean available
) {
    /**
     * Creates a HotelSearchResult from a Hotel entity.
     */
    public static HotelSearchResult fromEntity(Hotel hotel) {
        return new HotelSearchResult(
                hotel.getId().toString(),
                hotel.getName(),
                hotel.getCity(),
                hotel.getAddress(),
                hotel.getPricePerNight(),
                hotel.getRoomType(),
                hotel.getRating(),
                hotel.getAvailability()
        );
    }
}
