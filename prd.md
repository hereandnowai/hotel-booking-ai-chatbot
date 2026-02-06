# PRODUCT REQUIREMENTS DOCUMENT (PRD)

## AI-Powered Hotel Booking Chatbot

---

## 1) PRODUCT OVERVIEW

This product is an AI-powered conversational chatbot designed for the **travel and hospitality industry** that enables users (customers and travel agents) to search, book, modify, and cancel hotel reservations through natural language conversation.

The system will be **LLM-driven**, built using:

* **Java (Spring Boot + Spring AI)** as the backend framework
* **Ollama** for serving an open-source LLM (**GPT-OSS 20B**)
* **PostgreSQL** as the primary data store
* No third-party integrations in Phase 1 (all logic and data remain internal)

The chatbot should feel like a **smart hotel concierge + booking assistant + travel helper**.

---

## 2) PROBLEM STATEMENT

Users today:

* Struggle with complex booking websites
* Need to navigate multiple pages to complete a reservation
* Face difficulty modifying or canceling bookings
* Lack personalized hotel recommendations
* Need a faster, conversational experience

This chatbot solves these by allowing users to simply **talk in natural language** instead of clicking through menus.

---

## 3) TARGET USERS (PERSONAS)

### Persona A — Direct Customer

* Wants to book a hotel quickly
* Prefers chat over forms
* May ask: “Find me a 3-star hotel in Chennai near the beach”

### Persona B — Travel Agent

* Handles multiple client bookings
* Needs fast search, bulk booking, and modifications
* May ask: “Book 3 rooms in Bangalore for my client from March 10–12”

---

## 4) CORE PRODUCT GOAL

The chatbot must:

* Understand natural language
* Collect structured booking details from conversation
* Store bookings in PostgreSQL
* Confirm bookings clearly
* Allow edits and cancellations
* Provide helpful travel-related suggestions

---

## 5) KEY FEATURES (FUNCTIONAL REQUIREMENTS)

### A) Conversational Booking Flow

The chatbot must:

1. Ask clarifying questions when needed:

   * City
   * Check-in date
   * Check-out date
   * Number of guests
   * Room preference (single/double/suite)
   * Budget range

2. Example conversation:
   User: “Book a hotel in Chennai”
   Bot: “Sure! What dates?”
   User: “March 5 to March 7”
   Bot: “How many guests?”
   User: “Two”
   Bot: “Got it. Looking for options…”

### B) Search & Recommendation Engine (Internal)

The system should:

* Query PostgreSQL for available hotels
* Rank results based on:

  * Price
  * Location
  * Ratings (future feature)

### C) Booking Confirmation

Once the user agrees, the bot must:

* Create a booking record in PostgreSQL
* Generate a booking ID
* Return a confirmation message like:
  “Your booking is confirmed! ID: HBK-2025-00123”

### D) Modify Booking

User can say:
“Change my booking to March 10”
The bot must:

* Fetch the existing booking
* Update the date in PostgreSQL
* Confirm the update

### E) Cancel Booking

User can say:
“Cancel my booking”
The bot must:

* Ask for confirmation
* Mark booking as CANCELLED in the database

### F) Travel Assistance (Light)

The bot should be able to answer:

* “What’s the best time to visit Chennai?”
* “Recommend hotels near Marina Beach”

(This uses GPT-OSS general knowledge, not live data.)

---

## 6) DATA MODEL (POSTGRESQL)

### Table: users

* user_id (UUID, Primary Key)
* name (TEXT)
* email (TEXT)
* phone (TEXT)

### Table: hotels

* hotel_id (UUID, Primary Key)
* name (TEXT)
* city (TEXT)
* price_per_night (INTEGER)
* room_type (TEXT)
* availability (BOOLEAN)

### Table: bookings

* booking_id (UUID, Primary Key)
* user_id (FK)
* hotel_id (FK)
* check_in (DATE)
* check_out (DATE)
* guests (INTEGER)
* status (TEXT: CONFIRMED / CANCELLED / MODIFIED)
* created_at (TIMESTAMP)

---

## 7) TECH STACK DETAILS

### Backend

* Java 17+
* Spring Boot
* Spring AI
* REST APIs

### LLM Layer

* Ollama running locally
* Model: gpt-oss:20b
* Used for:

  * Understanding user intent
  * Generating responses
  * Asking follow-up questions

### Database

* PostgreSQL
* JPA / Hibernate

### Frontend (Phase 1)

* **React** (Vite-based)
* **Tailwind CSS** for a clean, modern chat interface
* **Lucide React** for icons
* **Axios** for API communication

---

## 8) SYSTEM ARCHITECTURE

React Frontend → Spring Boot API → Spring AI → Ollama (GPT-OSS) → PostgreSQL

---

## 9) SUCCESS METRICS (KPIs)

* Response time: < 2 seconds
* Booking success rate: > 95%
* User satisfaction: 4.5/5 (future survey)
* Error rate: < 2%

---

## 10) SECURITY & PRIVACY

* No passwords stored in plain text
* Secure database credentials
* Logging of conversations for debugging (optional)

---

## 11) FUTURE PHASES (OPTIONAL)

* Payment gateway integration
* Real-time hotel availability
* Multi-language support
* WhatsApp chatbot version

---

### END OF PRD
