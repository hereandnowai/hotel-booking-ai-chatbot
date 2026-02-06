package com.hotel.chatbot.service;

import com.hotel.chatbot.entity.Booking;
import com.hotel.chatbot.entity.BookingStatus;
import com.hotel.chatbot.entity.Hotel;
import com.hotel.chatbot.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for BookingService.
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;
    private Hotel testHotel;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository);
        testHotel = new Hotel("Test Hotel", "Chennai", 3500, "Double");
    }

    @Test
    @DisplayName("Should create a booking successfully")
    void createBooking_Success() {
        // Given
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        int guests = 2;

        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Booking booking = bookingService.createBooking(testHotel, checkIn, checkOut, guests, null);

        // Then
        assertThat(booking).isNotNull();
        assertThat(booking.getBookingReference()).matches("HBK-\\d{4}-\\d{5}");
        assertThat(booking.getHotel()).isEqualTo(testHotel);
        assertThat(booking.getCheckIn()).isEqualTo(checkIn);
        assertThat(booking.getCheckOut()).isEqualTo(checkOut);
        assertThat(booking.getGuests()).isEqualTo(guests);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    @DisplayName("Should calculate total price correctly")
    void createBooking_CalculatesTotalPrice() {
        // Given
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(4); // 3 nights
        int pricePerNight = 3500;

        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Booking booking = bookingService.createBooking(testHotel, checkIn, checkOut, 2, null);

        // Then
        assertThat(booking.getTotalPrice()).isEqualTo(3 * pricePerNight);
        assertThat(booking.getNumberOfNights()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should throw exception when hotel is null")
    void createBooking_NullHotel_ThrowsException() {
        // Given
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);

        // When/Then
        assertThatThrownBy(() -> bookingService.createBooking(null, checkIn, checkOut, 2, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hotel must not be null");
    }

    @Test
    @DisplayName("Should throw exception when check-out is before check-in")
    void createBooking_InvalidDates_ThrowsException() {
        // Given
        LocalDate checkIn = LocalDate.now().plusDays(3);
        LocalDate checkOut = LocalDate.now().plusDays(1); // Before check-in

        // When/Then
        assertThatThrownBy(() -> bookingService.createBooking(testHotel, checkIn, checkOut, 2, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Check-out date must be after check-in date");
    }

    @Test
    @DisplayName("Should throw exception when guests is zero or negative")
    void createBooking_InvalidGuests_ThrowsException() {
        // Given
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);

        // When/Then
        assertThatThrownBy(() -> bookingService.createBooking(testHotel, checkIn, checkOut, 0, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Number of guests must be at least 1");
    }

    @Test
    @DisplayName("Should generate unique booking references")
    void createBooking_GeneratesUniqueReferences() {
        // Given
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);

        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Booking booking1 = bookingService.createBooking(testHotel, checkIn, checkOut, 2, null);
        Booking booking2 = bookingService.createBooking(testHotel, checkIn, checkOut, 2, null);

        // Then
        assertThat(booking1.getBookingReference()).isNotEqualTo(booking2.getBookingReference());
    }
}
