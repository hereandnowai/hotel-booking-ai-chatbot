package com.hotel.chatbot.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a hotel booking in the system.
 * Links a user to a hotel for a specific date range.
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id")
    private UUID id;

    @Column(name = "booking_reference", nullable = false, unique = true, length = 20)
    private String bookingReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Integer guests = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Booking() {
        // JPA requires a no-arg constructor
    }

    public Booking(String bookingReference, Hotel hotel, LocalDate checkIn, LocalDate checkOut, Integer guests) {
        this.bookingReference = Objects.requireNonNull(bookingReference, "Booking reference must not be null");
        this.hotel = Objects.requireNonNull(hotel, "Hotel must not be null");
        this.checkIn = Objects.requireNonNull(checkIn, "Check-in date must not be null");
        this.checkOut = Objects.requireNonNull(checkOut, "Check-out date must not be null");
        this.guests = Objects.requireNonNull(guests, "Number of guests must not be null");
        this.status = BookingStatus.CONFIRMED;
        
        validateDates();
        calculateTotalPrice();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    private void validateDates() {
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    private void calculateTotalPrice() {
        if (hotel != null && hotel.getPricePerNight() != null) {
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            this.totalPrice = (int) (nights * hotel.getPricePerNight());
        }
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public User getUser() {
        return user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public Integer getGuests() {
        return guests;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        calculateTotalPrice();
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = Objects.requireNonNull(checkIn, "Check-in date must not be null");
        validateDates();
        calculateTotalPrice();
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = Objects.requireNonNull(checkOut, "Check-out date must not be null");
        validateDates();
        calculateTotalPrice();
    }

    public void setGuests(Integer guests) {
        this.guests = Objects.requireNonNull(guests, "Number of guests must not be null");
    }

    public void setStatus(BookingStatus status) {
        this.status = Objects.requireNonNull(status, "Status must not be null");
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Marks the booking as cancelled.
     */
    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    /**
     * Marks the booking as modified.
     */
    public void markAsModified() {
        this.status = BookingStatus.MODIFIED;
    }

    /**
     * Calculates the number of nights for this booking.
     */
    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Booking{id=" + id + ", bookingReference='" + bookingReference + 
               "', checkIn=" + checkIn + ", checkOut=" + checkOut + 
               ", guests=" + guests + ", status=" + status + "}";
    }
}
