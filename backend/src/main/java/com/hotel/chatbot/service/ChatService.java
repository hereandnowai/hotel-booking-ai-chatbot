package com.hotel.chatbot.service;

import com.hotel.chatbot.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

/**
 * Service for handling chat interactions with the AI model.
 * Uses Spring AI's ChatClient to communicate with Ollama.
 */
@Service
public class ChatService {

    private static final String SYSTEM_PROMPT = """
            You are a helpful and friendly AI hotel concierge assistant for a hotel booking system.
            Your name is HotelBot, and you work for a premium hotel booking platform.
            
            Your capabilities:
            1. Search for available hotels in various cities across India
            2. Help users book hotel rooms
            3. Modify existing bookings (change dates, number of guests)
            4. Cancel bookings when requested
            5. Provide travel recommendations and tips
            
            Guidelines:
            - Always be polite, professional, and helpful
            - When a user wants to book a hotel, collect these details:
              * City/Location
              * Check-in date
              * Check-out date
              * Number of guests
              * Room preference (single/double/suite) - optional
              * Budget range - optional
            - Ask one or two questions at a time, not all at once
            - If the user provides partial information, acknowledge what you have and ask for what's missing
            - When displaying hotel options, format them clearly with name, location, price, and room type
            - After a booking is confirmed, always provide the booking reference ID
            - For modifications or cancellations, ask for the booking reference ID first
            - Be conversational and natural in your responses
            - If you don't know something, be honest about it
            - For travel tips, use your general knowledge about Indian destinations
            
            Current date context: The system date should be considered for booking validations.
            Booking IDs follow the format: HBK-YYYY-XXXXX (e.g., HBK-2026-00123)
            
            Remember: You have access to tools for searching hotels, creating bookings, modifying bookings, and cancelling bookings.
            Use these tools when the user requests these actions and you have collected sufficient information.
            """;

    private final ChatClient chatClient;
    private final HotelBookingTools bookingTools;
    private final Map<String, Long> sessionLastAccess = new ConcurrentHashMap<>();

    public ChatService(ChatClient chatClient, HotelBookingTools bookingTools) {
        this.chatClient = chatClient;
        this.bookingTools = bookingTools;
    }

    /**
     * Processes a user message and returns the AI response.
     *
     * @param userMessage the user's message
     * @param sessionId   the session ID for conversation context
     * @return the chat response from the AI
     */
    public ChatResponse chat(String userMessage, String sessionId) {
        // Track session access
        sessionLastAccess.put(sessionId, System.currentTimeMillis());

        try {
            String response = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(userMessage)
                    .tools(bookingTools)
                    .advisors(advisor -> advisor.param(CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId))
                    .call()
                    .content();

            return new ChatResponse(response, null, null);
        } catch (Exception e) {
            return new ChatResponse(
                    "I apologize, but I encountered an issue processing your request. " +
                    "Please try again or rephrase your question.",
                    null,
                    null
            );
        }
    }

    /**
     * Clears the conversation history for a session.
     *
     * @param sessionId the session ID to clear
     */
    public void clearSession(String sessionId) {
        sessionLastAccess.remove(sessionId);
        // Note: In-memory chat memory doesn't persist beyond the ChatClient instance
        // For production, you'd want to implement a more sophisticated memory management
    }
}
