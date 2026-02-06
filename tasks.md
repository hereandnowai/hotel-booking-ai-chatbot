# Implementation Roadmap: AI-Powered Hotel Booking Chatbot

This document breaks down the development of the Hotel Booking Chatbot into actionable phases and tasks, adhering to the patterns defined in `prd.md` and the project coding instructions.

## Phase 1: Project Scaffolding & Infrastructure
Set up the core development environments for both backend and frontend.

- **Task 1.1: Backend Initialization (Spring Boot)**
    - Initialise Spring Boot project (Java 17, Maven).
    - Add dependencies: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-ai-ollama-spring-boot-starter`, `postgresql`.
    - Configure `application.properties` with PostgreSQL and Ollama (`gpt-oss:20b`) settings.
- **Task 1.2: Frontend Initialization (React)**
    - Create Vite project with React and TypeScript.
    - Install dependencies: `tailwindcss`, `axios`, `lucide-react`, `framer-motion` (for animations).
    - Configure Tailwind CSS.
- **Task 1.3: Database Setup**
    - Setup local/Docker PostgreSQL instance.
    - Create `hotel_chatbot` database.

## Phase 2: Domain Model & Data Access (JPA)
Define the schema and persistence layer as per Section 6 of the PRD.

- **Task 2.1: Entity Design**
    - Create `User` entity (UUID, name, email, phone).
    - Create `Hotel` entity (UUID, name, city, price_per_night, room_type, availability).
    - Create `Booking` entity with `BookingStatus` enum (`CONFIRMED`, `CANCELLED`, `MODIFIED`).
- **Task 2.2: Repositories**
    - Implement `HotelRepository` with custom search queries for city/location.
    - Implement `BookingRepository` and `UserRepository`.
- **Task 2.3: Data Seeding**
    - Create a `data.sql` or `CommandLineRunner` to populate the `hotels` table with sample data for testing.

## Phase 3: LLM Integration & Function Calling (Spring AI)
Connect the backend to Ollama and define the "intelligence" of the chatbot.

- **Task 3.1: Ollama Connection**
    - Verify connectivity to `gpt-oss:20b`.
    - Implement a basic `ChatService` using `ChatClient`.
- **Task 3.2: AI Function Calling (Tools)**
    - Define Java methods for AI functions: `searchHotels`, `createBooking`, `modifyBooking`, `cancelBooking`.
    - Register these functions with Spring AI so the LLM can trigger them.
- **Task 3.3: Prompt Engineering**
    - Design the System Prompt to enforce the "Smart Hotel Concierge" persona.
    - Ensure the LLM knows how to extract `check-in`, `check-out`, and `guest` counts.

## Phase 4: Backend Business Logic
Implement the core services and API endpoints.

- **Task 4.1: Booking Service**
    - Implement `HBK-YYYY-XXXXX` ID generation logic.
    - Implement business rules (e.g., check-out must be after check-in).
- **Task 4.2: Chat Controller**
    - Create `POST /api/chat` endpoint to handle user messages.
    - Implement session management or history tracking for conversational context.
- **Task 4.3: DTOs & Records**
    - Use Java Records for all request/response payloads (`ChatMessage`, `BookingRequest`).

## Phase 5: Frontend Development (Chat UI)
Build the user-facing natural language interface.

- **Task 5.1: Core UI Layout**
    - Design a modern chat bubble interface using Tailwind CSS.
    - Implement a messages list component.
- **Task 5.2: State Management**
    - Handle user message input and bot "typing" indicators.
    - Manage chat history in local state.
- **Task 5.3: API Integration**
    - Connect React components to `ChatController`.
    - Handle streaming vs. non-streaming responses (if applicable).

## Phase 6: Refinement & Validation
Apply standards and fix edge cases.

- **Task 6.1: Validation & Error Handling**
    - Add Spring Boot `@Valid` checks for booking inputs.
    - Implement global exception handler for API errors.
- **Task 6.2: Code Quality Audit**
    - Refactor code to follow `java-development.instructions.md`.
    - Ensure proper resource management and immutability (Records).
- **Task 6.3: Testing**
    - Write JUnit tests for `BookingService`.
    - Verify end-to-end flow: Search -> Book -> Confirm.

## Phase 7: Documentation & Handover
- **Task 7.1: README Update**
    - Document environment variables and setup steps.
- **Task 7.2: Final PRD Review**
    - Ensure all KPIs from Section 9 are theoretically met.
