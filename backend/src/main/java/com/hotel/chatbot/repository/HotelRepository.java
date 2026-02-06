package com.hotel.chatbot.repository;

import com.hotel.chatbot.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Hotel entity operations.
 * Includes custom search queries for location-based searches.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID> {

    /**
     * Finds all available hotels in a specific city.
     *
     * @param city the city to search in (case-insensitive)
     * @return list of available hotels in the city
     */
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.city) LIKE LOWER(CONCAT('%', :city, '%')) AND h.availability = true ORDER BY h.pricePerNight ASC")
    List<Hotel> findByCityContainingIgnoreCaseAndAvailabilityTrue(@Param("city") String city);

    /**
     * Finds all hotels in a city, regardless of availability.
     *
     * @param city the city to search in
     * @return list of hotels in the city
     */
    List<Hotel> findByCityIgnoreCase(String city);

    /**
     * Finds available hotels within a price range in a city.
     *
     * @param city the city to search in
     * @param minPrice minimum price per night
     * @param maxPrice maximum price per night
     * @return list of hotels matching the criteria
     */
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.city) LIKE LOWER(CONCAT('%', :city, '%')) " +
           "AND h.availability = true " +
           "AND h.pricePerNight >= :minPrice AND h.pricePerNight <= :maxPrice " +
           "ORDER BY h.pricePerNight ASC")
    List<Hotel> findAvailableByPriceRange(@Param("city") String city, 
                                           @Param("minPrice") Integer minPrice, 
                                           @Param("maxPrice") Integer maxPrice);

    /**
     * Finds available hotels by room type in a city.
     *
     * @param city the city to search in
     * @param roomType the type of room (single, double, suite)
     * @return list of hotels matching the criteria
     */
    @Query("SELECT h FROM Hotel h WHERE LOWER(h.city) LIKE LOWER(CONCAT('%', :city, '%')) " +
           "AND h.availability = true " +
           "AND LOWER(h.roomType) = LOWER(:roomType) " +
           "ORDER BY h.pricePerNight ASC")
    List<Hotel> findAvailableByRoomType(@Param("city") String city, @Param("roomType") String roomType);

    /**
     * Finds all available hotels ordered by rating.
     *
     * @return list of available hotels sorted by rating descending
     */
    @Query("SELECT h FROM Hotel h WHERE h.availability = true ORDER BY h.rating DESC NULLS LAST")
    List<Hotel> findAllAvailableOrderByRating();

    /**
     * Search hotels by name or city (for general search).
     *
     * @param searchTerm the search term to match against name or city
     * @return list of matching hotels
     */
    @Query("SELECT h FROM Hotel h WHERE h.availability = true " +
           "AND (LOWER(h.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(h.city) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY h.pricePerNight ASC")
    List<Hotel> searchHotels(@Param("searchTerm") String searchTerm);

    /**
     * Finds all available hotels.
     *
     * @return list of all available hotels
     */
    List<Hotel> findByAvailabilityTrue();
}
