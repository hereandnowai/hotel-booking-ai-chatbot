package com.hotel.chatbot.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a hotel in the booking system.
 * Hotels have rooms available for booking with various attributes.
 */
@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hotel_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column
    private String address;

    @Column(name = "price_per_night", nullable = false)
    private Integer pricePerNight;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column
    private Boolean availability = true;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    protected Hotel() {
        // JPA requires a no-arg constructor
    }

    public Hotel(String name, String city, Integer pricePerNight, String roomType) {
        this.name = Objects.requireNonNull(name, "Hotel name must not be null");
        this.city = Objects.requireNonNull(city, "City must not be null");
        this.pricePerNight = Objects.requireNonNull(pricePerNight, "Price per night must not be null");
        this.roomType = Objects.requireNonNull(roomType, "Room type must not be null");
        this.availability = true;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPricePerNight() {
        return pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Hotel name must not be null");
    }

    public void setCity(String city) {
        this.city = Objects.requireNonNull(city, "City must not be null");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPricePerNight(Integer pricePerNight) {
        this.pricePerNight = Objects.requireNonNull(pricePerNight, "Price per night must not be null");
    }

    public void setRoomType(String roomType) {
        this.roomType = Objects.requireNonNull(roomType, "Room type must not be null");
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Hotel{id=" + id + ", name='" + name + "', city='" + city + 
               "', pricePerNight=" + pricePerNight + ", roomType='" + roomType + "'}";
    }
}
