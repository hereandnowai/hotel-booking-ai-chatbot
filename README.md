<p align="center">
  <img src="https://raw.githubusercontent.com/hereandnowai/images/refs/heads/main/logos/logo-of-here-and-now-ai.png" alt="HERE AND NOW AI Logo" width="300">
</p>

# HERE AND NOW AI: Intelligent Hotel Booking Chatbot 🏨

> **"AI is Good"**

An intelligent conversational chatbot for hotel booking built with **Spring Boot**, **Spring AI**, **Ollama**, and **React**. Experience the future of travel planning where users can search for hotels, make bookings, modify reservations, and cancel them using natural language.

## 🌟 Features

- **Empathetic NLP**: Powered by Ollama (GPT-OSS 20B) for human-like understanding of user intent.
- **Smart Hotel Discovery**: Instant hotel search by city, price range, and room type.
- **Seamless Booking Lifecycle**: Effortlessly create, modify, and cancel hotel reservations.
- **Deep Conversational Context**: Maintains multi-turn dialogue history for a personalized experience.
- **Spring AI Orchestration**: Leveraging advanced AI function calling for secure database operations.
- **Stunning UI**: A modern, responsive React interface styled with Tailwind CSS and the HERE AND NOW AI brand identity.

## 🏗️ Architecture

```
┌─────────────────────────┐     ┌───────────────────────┐     ┌──────────────────────┐
│   React Frontend UI     │────▶│   Spring Boot         │────▶│      Ollama          │
│   (Vite + TypeScript)   │     │   REST Service        │     │    (GPT-OSS 20B)     │
└─────────────────────────┘     └──────────┬────────────┘     └──────────────────────┘
                                           │
                                           ▼
                                ┌───────────────────────┐
                                │    PostgreSQL         │
                                │    Storage Layer      │
                                └───────────────────────┘
```

## 📋 Prerequisites

- **Java 17+** (JDK)
- **Node.js 18+** (for React frontend)
- **PostgreSQL 15+** (or Docker)
- **Ollama** with `gpt-oss:20b` model

## 🚀 Quick Start

### 1. Database Setup

Using Docker (recommended):
```bash
cd database
docker-compose up -d
```

Or manually create PostgreSQL database:
```sql
CREATE DATABASE hotel_chatbot;
```

### 2. Start Ollama

Make sure Ollama is running with the required model:
```bash
ollama run gpt-oss:20b
```

### 3. Backend Setup

```bash
cd backend
chmod +x mvnw

# Run the application
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

### 4. Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

The frontend will start on `http://localhost:5173`

## 🔧 Configuration

### Backend (`backend/src/main/resources/application.properties`)

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_chatbot
spring.datasource.username=postgres
spring.datasource.password=postgres

# Ollama
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=gpt-oss:20b
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/hotel_chatbot` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `postgres` |
| `SPRING_AI_OLLAMA_BASE_URL` | Ollama server URL | `http://localhost:11434` |

## 💬 Usage Examples

### Searching for Hotels
```
User: "Find hotels in Chennai"
Bot: "I found 5 hotels in Chennai. Here are the options..."

User: "Show me hotels under ₹4000 in Bangalore"
Bot: "Here are the available hotels in Bangalore under ₹4000..."
```

### Making a Booking
```
User: "Book a hotel in Goa for 2 guests"
Bot: "Sure! What dates would you like to check in and check out?"

User: "March 10 to March 15"
Bot: "Got it! Here are the available options..."

User: "I'll take the Calangute Beach Resort"
Bot: "✅ Booking Confirmed! ID: HBK-2026-00001..."
```

### Modifying a Booking
```
User: "Change my booking HBK-2026-00001 to March 12"
Bot: "✅ Booking Modified Successfully!..."
```

### Cancelling a Booking
```
User: "Cancel booking HBK-2026-00001"
Bot: "✅ Booking Cancelled Successfully..."
```

## 📁 Project Structure

```
chatbot/
├── backend/                    # Spring Boot application
│   ├── src/main/java/com/hotel/chatbot/
│   │   ├── config/            # Configuration classes
│   │   ├── controller/        # REST controllers
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── entity/            # JPA entities
│   │   ├── exception/         # Exception handling
│   │   ├── repository/        # JPA repositories
│   │   └── service/           # Business logic & AI tools
│   └── src/test/              # Unit & integration tests
├── frontend/                   # React application
│   ├── src/
│   │   ├── components/        # React components
│   │   ├── services/          # API services
│   │   └── types/             # TypeScript types
│   └── index.html
├── database/                   # Database scripts
│   ├── docker-compose.yml
│   └── init.sql
└── prd.md                     # Product Requirements Document
```

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 📊 Data Model

### Users
| Field | Type | Description |
|-------|------|-------------|
| user_id | UUID | Primary key |
| name | TEXT | User's name |
| email | TEXT | Email address |
| phone | TEXT | Phone number |

### Hotels
| Field | Type | Description |
|-------|------|-------------|
| hotel_id | UUID | Primary key |
| name | TEXT | Hotel name |
| city | TEXT | City location |
| price_per_night | INTEGER | Price in INR |
| room_type | TEXT | single/double/suite |
| availability | BOOLEAN | Is available |

### Bookings
| Field | Type | Description |
|-------|------|-------------|
| booking_id | UUID | Primary key |
| booking_reference | TEXT | HBK-YYYY-XXXXX format |
| hotel_id | UUID | Foreign key to hotels |
| check_in | DATE | Check-in date |
| check_out | DATE | Check-out date |
| guests | INTEGER | Number of guests |
| status | TEXT | CONFIRMED/CANCELLED/MODIFIED |

## 🔒 API Endpoints

### Chat API
- `POST /api/chat` - Send a message to the chatbot
- `DELETE /api/chat/session/{sessionId}` - Clear session history

### Health Check
- `GET /api/health` - Application health status

## 🛠️ Development

### Building for Production

Backend:
```bash
cd backend
./mvnw clean package -DskipTests
java -jar target/chatbot-1.0.0.jar
```

Frontend:
```bash
cd frontend
npm run build
# Static files will be in dist/
```

## 📝 License

This project is for educational purposes.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

---

## 🌐 Connect with Us

Experience the future of AI with **HERE AND NOW AI**.

| Platform | Link |
| :--- | :--- |
| **Website** | [hereandnowai.com](https://hereandnowai.com) |
| **LinkedIn** | [linkedin.com/company/hereandnowai](https://www.linkedin.com/company/hereandnowai/) |
| **X (Twitter)** | [x.com/hereandnow_ai](https://x.com/hereandnow_ai) |
| **Instagram** | [instagram.com/hereandnow_ai](https://instagram.com/hereandnow_ai) |
| **YouTube** | [youtube.com/@hereandnow_ai](https://youtube.com/@hereandnow_ai) |
| **GitHub** | [github.com/hereandnowai](https://github.com/hereandnowai) |
| **Email** | [info@hereandnowai.com](mailto:info@hereandnowai.com) |
| **Phone** | +91 996 296 1000 |

<p align="center">
  <b>Built by HERE AND NOW AI — AI is Good</b>
</p>
