package com.hotel.chatbot.repository;

import com.hotel.chatbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email.
     *
     * @param email the email address to check
     * @return true if a user exists with this email
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by their phone number.
     *
     * @param phone the phone number to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findByPhone(String phone);
}
