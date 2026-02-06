package com.hotel.chatbot.controller;

import com.hotel.chatbot.dto.ChatRequest;
import com.hotel.chatbot.dto.ChatResponse;
import com.hotel.chatbot.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for ChatController.
 */
@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Test
    @DisplayName("Should return chat response for valid message")
    void chat_ValidMessage_ReturnsResponse() throws Exception {
        // Given
        String responseMessage = "I found 5 hotels in Chennai. Here are the options...";
        when(chatService.chat(anyString(), anyString()))
                .thenReturn(new ChatResponse(responseMessage, null, null));

        // When/Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "message": "Find hotels in Chennai",
                                    "sessionId": "test-session"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(responseMessage));
    }

    @Test
    @DisplayName("Should return 400 for blank message")
    void chat_BlankMessage_ReturnsBadRequest() throws Exception {
        // When/Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "message": "",
                                    "sessionId": "test-session"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for missing message field")
    void chat_MissingMessage_ReturnsBadRequest() throws Exception {
        // When/Then
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "sessionId": "test-session"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
