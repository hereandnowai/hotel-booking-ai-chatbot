package com.hotel.chatbot.exception;

import java.time.LocalDateTime;

/**
 * Standard error response DTO.
 *
 * @param status    HTTP status code
 * @param error     error type
 * @param message   detailed error message
 * @param timestamp when the error occurred
 */
public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {
}
