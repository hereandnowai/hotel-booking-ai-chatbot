# Backend - Spring Boot Application

## Overview

This is the backend service for the AI-powered Hotel Booking Chatbot, built with Spring Boot 3.x and Spring AI.

## Technology Stack

- **Java 17**
- **Spring Boot 3.4.2**
- **Spring AI** (Ollama integration)
- **Spring Data JPA** (PostgreSQL)
- **Maven** (Build tool)

## Getting Started

### Prerequisites

1. Java 17+ installed
2. PostgreSQL running on localhost:5432
3. Ollama running with `gpt-oss:20b` model

### Running the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=BookingServiceTest

# Run with coverage
./mvnw test jacoco:report
```

### Building

```bash
# Build JAR
./mvnw clean package

# Skip tests
./mvnw clean package -DskipTests
```

## Project Structure

```
src/main/java/com/hotel/chatbot/
├── HotelChatbotApplication.java    # Main application class
├── config/
│   ├── AiConfig.java               # Spring AI configuration
│   ├── CorsConfig.java             # CORS settings
│   └── DataSeeder.java             # Database seeding
├── controller/
│   ├── ChatController.java         # Chat API endpoint
│   └── HealthController.java       # Health check endpoint
├── dto/
│   ├── ChatRequest.java            # Chat request DTO
│   ├── ChatResponse.java           # Chat response DTO
│   ├── BookingInfo.java            # Booking info DTO
│   └── HotelSearchResult.java      # Hotel search DTO
├── entity/
│   ├── User.java                   # User entity
│   ├── Hotel.java                  # Hotel entity
│   ├── Booking.java                # Booking entity
│   └── BookingStatus.java          # Booking status enum
├── exception/
│   ├── GlobalExceptionHandler.java # Exception handling
│   ├── ErrorResponse.java          # Error response DTO
│   └── BookingNotFoundException.java
├── repository/
│   ├── UserRepository.java
│   ├── HotelRepository.java
│   └── BookingRepository.java
└── service/
    ├── ChatService.java            # Chat orchestration
    ├── BookingService.java         # Booking business logic
    └── HotelBookingTools.java      # AI function calling tools
```

## Configuration

### application.properties

Key configuration options:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_chatbot
spring.datasource.username=postgres
spring.datasource.password=postgres

# Ollama
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=gpt-oss:20b
spring.ai.ollama.chat.options.temperature=0.7

# CORS
app.cors.allowed-origins=http://localhost:5173
```

## AI Function Calling

The `HotelBookingTools` class exposes the following tools to the AI:

| Tool | Description |
|------|-------------|
| `searchHotels` | Search for available hotels by city, price, room type |
| `createBooking` | Create a new hotel booking |
| `modifyBooking` | Modify an existing booking |
| `cancelBooking` | Cancel a booking |
| `getBookingDetails` | Get details of a booking |

## API Documentation

### POST /api/chat

Send a message to the chatbot.

**Request:**
```json
{
  "message": "Find hotels in Chennai",
  "sessionId": "optional-session-id"
}
```

**Response:**
```json
{
  "message": "I found 5 hotels in Chennai...",
  "bookingInfo": null,
  "hotels": null
}
```

### GET /api/health

Check application health.

**Response:**
```json
{
  "status": "UP",
  "timestamp": "2026-02-05T10:30:00",
  "service": "Hotel Booking Chatbot"
}
```
