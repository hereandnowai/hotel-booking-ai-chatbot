# AI Coding Instructions for Hotel Booking Chatbot

This project is an AI-powered hotel booking system using Spring Boot, Spring AI, Ollama, and React.

## Big Picture Architecture
- **Frontend**: React (Vite, Tailwind CSS, Lucide Icons) - A modern chat interface.
- **Backend**: Java (Spring Boot 3.x, Spring AI) - Handles business logic and LLM orchestration.
- **LLM Layer**: Ollama running locally with `gpt-oss:20b`.
- **Database**: PostgreSQL for users, hotels, and booking records.
- **Communication**: React → Spring Boot REST API → Spring AI → Ollama.

## Core Patterns & Conventions
- **LLM Intent Handing**: Use Spring AI's `ChatClient` to interact with Ollama. Focus on extracting entities (city, dates, guests) from natural language.
- **Function Calling**: Use Spring AI function calling to trigger database actions (search, book, modify, cancel) directly from the LLM context.
- **Data Access**: Use Spring Data JPA for PostgreSQL interactions. Entity relationships: `Booking` links `User` and `Hotel`.
- **Chat UI**: Implement a message-stream pattern. Each message should have a `sender` (user/bot) and `timestamp`.

## Development Workflow
- **Backend**:
  - Run: `./mvnw spring-boot:run`
  - Tests: `./mvnw test`
- **Frontend**:
  - Install: `npm install`
  - Run: `npm run dev`
- **Database**: Ensure PostgreSQL is running and connection details are in `application.properties`.
- **Ollama**: Must be running locally with `ollama run gpt-oss:20b`.

## Project Specifics
- **Booking IDs**: Follow the pattern `HBK-YYYY-XXXXX`.
- **Status Enum**: Bookings must use `CONFIRMED`, `CANCELLED`, or `MODIFIED`.
- **Location**: Use PostgreSQL's text search for hotel location queries.

## Key Files to Reference
- `prd.md`: Primary source of truth for features and data models.
- `src/main/resources/application.properties`: Configuration for Spring AI and DB.
- `src/main/java/.../controller/ChatController.java`: Entry point for chat interactions.
