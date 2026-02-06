package com.hotel.chatbot.controller;

import com.hotel.chatbot.dto.ChatRequest;
import com.hotel.chatbot.dto.ChatResponse;
import com.hotel.chatbot.service.ChatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for chat interactions.
 * Handles communication between the React frontend and the AI chatbot.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Processes a user chat message and returns the AI response.
     *
     * @param request the chat request containing the user's message
     * @return the AI response
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.debug("Received chat message: {}", request.message());
        
        ChatResponse response = chatService.chat(request.message(), request.sessionId());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Clears the chat session history.
     *
     * @param sessionId the session ID to clear
     * @return no content response
     */
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> clearSession(@PathVariable String sessionId) {
        log.debug("Clearing session: {}", sessionId);
        
        chatService.clearSession(sessionId);
        
        return ResponseEntity.noContent().build();
    }
}
