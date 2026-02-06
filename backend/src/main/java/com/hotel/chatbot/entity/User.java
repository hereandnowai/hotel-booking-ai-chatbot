package com.hotel.chatbot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity representing a user in the hotel booking system.
 * Users can make, modify, and cancel hotel bookings.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phone;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    protected User() {
        // JPA requires a no-arg constructor
    }

    public User(String name, String email, String phone) {
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.email = Objects.requireNonNull(email, "Email must not be null");
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters for mutable fields
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name must not be null");
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "Email must not be null");
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
