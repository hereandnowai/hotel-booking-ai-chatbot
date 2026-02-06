package com.hotel.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Hotel Booking Chatbot application.
 * This application provides an AI-powered conversational interface
 * for hotel search, booking, modification, and cancellation.
 */
@SpringBootApplication
public class HotelChatbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelChatbotApplication.class, args);
    }
}
