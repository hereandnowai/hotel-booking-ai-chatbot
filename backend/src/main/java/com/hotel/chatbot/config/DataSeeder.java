package com.hotel.chatbot.config;

import com.hotel.chatbot.entity.Hotel;
import com.hotel.chatbot.entity.User;
import com.hotel.chatbot.repository.HotelRepository;
import com.hotel.chatbot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;

/**
 * Configuration class that seeds the database with sample data on startup.
 * Only runs when the database is empty.
 */
@Configuration
public class DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    @Profile("!test")
    CommandLineRunner seedDatabase(HotelRepository hotelRepository, UserRepository userRepository) {
        return args -> {
            // Only seed if the hotels table is empty
            if (hotelRepository.count() == 0) {
                log.info("Seeding database with sample hotels...");
                
                List<Hotel> hotels = createSampleHotels();
                hotelRepository.saveAll(hotels);
                
                log.info("Seeded {} hotels successfully", hotels.size());
            } else {
                log.info("Database already contains hotels, skipping seeding");
            }

            // Create a demo user if none exists
            if (userRepository.count() == 0) {
                log.info("Creating demo user...");
                var demoUser = new User("Demo User", "demo@hotel.com", "+91-9876543210");
                userRepository.save(demoUser);
                log.info("Demo user created successfully");
            }
        };
    }

    private List<Hotel> createSampleHotels() {
        return List.of(
            // Chennai Hotels
            createHotel("The Grand Chennai", "Chennai", "Marina Beach Road", 4500, "Double", new BigDecimal("4.5")),
            createHotel("Chennai Palace Inn", "Chennai", "T. Nagar Main Road", 3200, "Single", new BigDecimal("4.2")),
            createHotel("Marina Bay Resort", "Chennai", "ECR Beach", 6800, "Suite", new BigDecimal("4.7")),
            createHotel("Budget Stay Chennai", "Chennai", "Central Station", 1800, "Single", new BigDecimal("3.8")),
            createHotel("Seaside Retreat", "Chennai", "Besant Nagar Beach", 5200, "Double", new BigDecimal("4.4")),
            
            // Bangalore Hotels
            createHotel("Bangalore Tech Hub", "Bangalore", "Electronic City", 3800, "Double", new BigDecimal("4.3")),
            createHotel("Garden City Suites", "Bangalore", "MG Road", 7500, "Suite", new BigDecimal("4.8")),
            createHotel("Koramangala Inn", "Bangalore", "Koramangala 5th Block", 2800, "Single", new BigDecimal("4.0")),
            createHotel("Brigade Residency", "Bangalore", "Brigade Road", 4200, "Double", new BigDecimal("4.4")),
            createHotel("Whitefield Business Hotel", "Bangalore", "Whitefield", 3500, "Single", new BigDecimal("4.1")),
            
            // Mumbai Hotels
            createHotel("Gateway Grand Mumbai", "Mumbai", "Colaba, Near Gateway", 9500, "Suite", new BigDecimal("4.9")),
            createHotel("Bandra Bay View", "Mumbai", "Bandra West", 5800, "Double", new BigDecimal("4.5")),
            createHotel("Andheri Business Stay", "Mumbai", "Andheri East", 3200, "Single", new BigDecimal("4.0")),
            createHotel("Juhu Beach Resort", "Mumbai", "Juhu Beach", 7200, "Double", new BigDecimal("4.6")),
            createHotel("Lower Parel Inn", "Mumbai", "Lower Parel", 4500, "Single", new BigDecimal("4.2")),
            
            // Delhi Hotels
            createHotel("Delhi Imperial", "Delhi", "Connaught Place", 8500, "Suite", new BigDecimal("4.7")),
            createHotel("Karol Bagh Budget", "Delhi", "Karol Bagh", 2200, "Single", new BigDecimal("3.9")),
            createHotel("Aerocity Premium", "Delhi", "Aerocity, IGI Airport", 6500, "Double", new BigDecimal("4.5")),
            createHotel("South Delhi Residence", "Delhi", "Greater Kailash", 4800, "Double", new BigDecimal("4.3")),
            createHotel("Old Delhi Heritage", "Delhi", "Chandni Chowk", 3000, "Single", new BigDecimal("4.1")),
            
            // Goa Hotels
            createHotel("Calangute Beach Resort", "Goa", "Calangute Beach", 5500, "Double", new BigDecimal("4.4")),
            createHotel("Baga Sunset Villa", "Goa", "Baga Beach", 6200, "Suite", new BigDecimal("4.6")),
            createHotel("Panjim City Stay", "Goa", "Panjim City Center", 2800, "Single", new BigDecimal("4.0")),
            createHotel("Anjuna Bohemian", "Goa", "Anjuna Beach", 3800, "Double", new BigDecimal("4.3")),
            createHotel("South Goa Serenity", "Goa", "Palolem Beach", 4500, "Double", new BigDecimal("4.5")),
            
            // Hyderabad Hotels
            createHotel("Hyderabad Deccan", "Hyderabad", "Banjara Hills", 5200, "Double", new BigDecimal("4.4")),
            createHotel("Hitech City Suites", "Hyderabad", "Hitech City", 4500, "Suite", new BigDecimal("4.3")),
            createHotel("Charminar Heritage", "Hyderabad", "Old City", 2500, "Single", new BigDecimal("4.1")),
            createHotel("Gachibowli Business Hotel", "Hyderabad", "Gachibowli", 3800, "Double", new BigDecimal("4.2")),
            
            // Jaipur Hotels
            createHotel("Pink City Palace", "Jaipur", "Near Hawa Mahal", 6500, "Suite", new BigDecimal("4.7")),
            createHotel("Jaipur Heritage Inn", "Jaipur", "MI Road", 3200, "Double", new BigDecimal("4.3")),
            createHotel("Amer Fort View", "Jaipur", "Amer Road", 4800, "Double", new BigDecimal("4.5")),
            createHotel("Budget Jaipur Stay", "Jaipur", "Railway Station Road", 1900, "Single", new BigDecimal("3.8"))
        );
    }

    private Hotel createHotel(String name, String city, String address, int pricePerNight, 
                               String roomType, BigDecimal rating) {
        var hotel = new Hotel(name, city, pricePerNight, roomType);
        hotel.setAddress(address);
        hotel.setRating(rating);
        hotel.setAvailability(true);
        return hotel;
    }
}
