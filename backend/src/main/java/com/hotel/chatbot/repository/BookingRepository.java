package com.hotel.chatbot.repository;

import com.hotel.chatbot.entity.Booking;
import com.hotel.chatbot.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Booking entity operations.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    /**
     * Finds a booking by its reference ID (e.g., HBK-2025-00123).
     *
     * @param bookingReference the booking reference to search for
     * @return an Optional containing the booking if found
     */
    Optional<Booking> findByBookingReference(String bookingReference);

    /**
     * Finds all bookings for a specific user.
     *
     * @param userId the user's ID
     * @return list of bookings for the user
     */
    List<Booking> findByUserId(UUID userId);

    /**
     * Finds all bookings with a specific status.
     *
     * @param status the booking status to filter by
     * @return list of bookings with the given status
     */
    List<Booking> findByStatus(BookingStatus status);

    /**
     * Finds all active (confirmed or modified) bookings for a user.
     *
     * @param userId the user's ID
     * @return list of active bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status IN ('CONFIRMED', 'MODIFIED') ORDER BY b.checkIn ASC")
    List<Booking> findActiveBookingsByUser(@Param("userId") UUID userId);

    /**
     * Finds bookings for a hotel in a date range (for availability checking).
     *
     * @param hotelId the hotel's ID
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of overlapping bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.hotel.id = :hotelId " +
           "AND b.status != 'CANCELLED' " +
           "AND ((b.checkIn <= :endDate AND b.checkOut >= :startDate))")
    List<Booking> findOverlappingBookings(@Param("hotelId") UUID hotelId, 
                                           @Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);

    /**
     * Counts the number of bookings created today.
     * Used for generating sequential booking references.
     *
     * @param startOfDay the start of today
     * @return count of bookings created today
     */
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.createdAt >= :startOfDay")
    long countBookingsCreatedToday(@Param("startOfDay") java.time.LocalDateTime startOfDay);

    /**
     * Finds bookings by hotel ID.
     *
     * @param hotelId the hotel's ID
     * @return list of bookings for the hotel
     */
    List<Booking> findByHotelId(UUID hotelId);

    /**
     * Finds upcoming bookings (check-in date is in the future).
     *
     * @param today today's date
     * @return list of upcoming bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.checkIn >= :today AND b.status = 'CONFIRMED' ORDER BY b.checkIn ASC")
    List<Booking> findUpcomingBookings(@Param("today") LocalDate today);
}
