package com.hotel.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for chat messages.
 *
 * @param message   the user's message
 * @param sessionId optional session ID for conversation context
 */
public record ChatRequest(
        @NotBlank(message = "Message cannot be blank")
        @Size(max = 2000, message = "Message cannot exceed 2000 characters")
        String message,
        
        String sessionId
) {
    /**
     * Creates a ChatRequest with a default session ID if none provided.
     */
    public ChatRequest {
        if (sessionId == null || sessionId.isBlank()) {
            sessionId = "default-session";
        }
    }
}
