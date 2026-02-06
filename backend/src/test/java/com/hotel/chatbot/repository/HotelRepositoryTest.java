package com.hotel.chatbot.repository;

import com.hotel.chatbot.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for HotelRepository.
 */
@DataJpaTest
@ActiveProfiles("test")
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        hotelRepository.deleteAll();
        
        // Seed test data
        hotelRepository.saveAll(List.of(
                createHotel("Grand Chennai", "Chennai", 4500, "Double"),
                createHotel("Budget Chennai Inn", "Chennai", 2000, "Single"),
                createHotel("Bangalore Tech Hub", "Bangalore", 3500, "Suite"),
                createHotel("Chennai Beach Resort", "Chennai", 6000, "Suite")
        ));
    }

    @Test
    @DisplayName("Should find hotels by city")
    void findByCityContainingIgnoreCaseAndAvailabilityTrue_FindsHotels() {
        // When
        List<Hotel> hotels = hotelRepository.findByCityContainingIgnoreCaseAndAvailabilityTrue("chennai");

        // Then
        assertThat(hotels).hasSize(3);
        assertThat(hotels).allMatch(h -> h.getCity().equalsIgnoreCase("Chennai"));
    }

    @Test
    @DisplayName("Should find hotels within price range")
    void findAvailableByPriceRange_FindsHotelsInRange() {
        // When
        List<Hotel> hotels = hotelRepository.findAvailableByPriceRange("Chennai", 2000, 5000);

        // Then
        assertThat(hotels).hasSize(2);
        assertThat(hotels).allMatch(h -> h.getPricePerNight() >= 2000 && h.getPricePerNight() <= 5000);
    }

    @Test
    @DisplayName("Should find hotels by room type")
    void findAvailableByRoomType_FindsMatchingRoomType() {
        // When
        List<Hotel> hotels = hotelRepository.findAvailableByRoomType("Chennai", "Suite");

        // Then
        assertThat(hotels).hasSize(1);
        assertThat(hotels.get(0).getRoomType()).isEqualTo("Suite");
    }

    @Test
    @DisplayName("Should search hotels by name or city")
    void searchHotels_FindsMatchingHotels() {
        // When
        List<Hotel> hotels = hotelRepository.searchHotels("Grand");

        // Then
        assertThat(hotels).hasSize(1);
        assertThat(hotels.get(0).getName()).contains("Grand");
    }

    @Test
    @DisplayName("Should return empty list when no hotels match")
    void findByCityContainingIgnoreCaseAndAvailabilityTrue_NoMatch_ReturnsEmptyList() {
        // When
        List<Hotel> hotels = hotelRepository.findByCityContainingIgnoreCaseAndAvailabilityTrue("NonExistentCity");

        // Then
        assertThat(hotels).isEmpty();
    }

    private Hotel createHotel(String name, String city, int price, String roomType) {
        var hotel = new Hotel(name, city, price, roomType);
        hotel.setRating(new BigDecimal("4.0"));
        hotel.setAvailability(true);
        return hotel;
    }
}
