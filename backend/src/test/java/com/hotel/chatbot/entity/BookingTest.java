package com.hotel.chatbot.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for Booking entity.
 */
class BookingTest {

    @Test
    @DisplayName("Should create booking with valid data")
    void createBooking_ValidData_Success() {
        // Given
        Hotel hotel = new Hotel("Test Hotel", "Chennai", 3000, "Double");
        LocalDate checkIn = LocalDate.of(2026, 3, 10);
        LocalDate checkOut = LocalDate.of(2026, 3, 12);

        // When
        Booking booking = new Booking("HBK-2026-00001", hotel, checkIn, checkOut, 2);

        // Then
        assertThat(booking.getBookingReference()).isEqualTo("HBK-2026-00001");
        assertThat(booking.getHotel()).isEqualTo(hotel);
        assertThat(booking.getCheckIn()).isEqualTo(checkIn);
        assertThat(booking.getCheckOut()).isEqualTo(checkOut);
        assertThat(booking.getGuests()).isEqualTo(2);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    @DisplayName("Should calculate number of nights correctly")
    void getNumberOfNights_ReturnsCorrectValue() {
        // Given
        Hotel hotel = new Hotel("Test Hotel", "Chennai", 3000, "Double");
        LocalDate checkIn = LocalDate.of(2026, 3, 10);
        LocalDate checkOut = LocalDate.of(2026, 3, 15); // 5 nights

        Booking booking = new Booking("HBK-2026-00001", hotel, checkIn, checkOut, 2);

        // When
        long nights = booking.getNumberOfNights();

        // Then
        assertThat(nights).isEqualTo(5);
    }

    @Test
    @DisplayName("Should calculate total price correctly")
    void getTotalPrice_ReturnsCorrectValue() {
        // Given
        Hotel hotel = new Hotel("Test Hotel", "Chennai", 2500, "Double");
        LocalDate checkIn = LocalDate.of(2026, 3, 10);
        LocalDate checkOut = LocalDate.of(2026, 3, 13); // 3 nights

        // When
        Booking booking = new Booking("HBK-2026-00001", hotel, checkIn, checkOut, 2);

        // Then
        assertThat(booking.getTotalPrice()).isEqualTo(3 * 2500);
    }

    @Test
    @DisplayName("Should throw exception when check-out is before check-in")
    void createBooking_InvalidDates_ThrowsException() {
        // Given
        Hotel hotel = new Hotel("Test Hotel", "Chennai", 3000, "Double");
        LocalDate checkIn = LocalDate.of(2026, 3, 15);
        LocalDate checkOut = LocalDate.of(2026, 3, 10); // Before check-in

        // When/Then
        assertThatThrownBy(() -> new Booking("HBK-2026-00001", hotel, checkIn, checkOut, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Check-out date must be after check-in date");
    }

    @Test
    @DisplayName("Should cancel booking successfully")
    void cancel_ChangesStatusToCancelled() {
        // Given
        Hotel hotel = new Hotel("Test Hotel", "Chennai", 3000, "Double");
        Booking booking = new Booking("HBK-2026-00001", hotel, 
                LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 12), 2);

        // When
        booking.cancel();

        // Then
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    @DisplayName("Should mark booking as modified")
    void markAsModified_ChangesStatusToModified() {
        // Given
        Hotel hotel = new Hotel("Test Hotel", "Chennai", 3000, "Double");
        Booking booking = new Booking("HBK-2026-00001", hotel, 
                LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 12), 2);

        // When
        booking.markAsModified();

        // Then
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.MODIFIED);
    }
}
