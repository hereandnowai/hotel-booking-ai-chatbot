# Frontend - React Application

## Overview

Modern chat interface for the Hotel Booking Chatbot, built with React, TypeScript, and Tailwind CSS.

## Technology Stack

- **React 18.3**
- **TypeScript 5.5**
- **Vite** (Build tool)
- **Tailwind CSS** (Styling)
- **Framer Motion** (Animations)
- **Lucide React** (Icons)
- **Axios** (HTTP client)

## Getting Started

### Prerequisites

- Node.js 18+ installed

### Installation

```bash
# Install dependencies
npm install

# Start development server
npm run dev
```

The app will be available at `http://localhost:5173`

### Building for Production

```bash
# Build static files
npm run build

# Preview production build
npm run preview
```

## Project Structure

```
src/
├── main.tsx                    # Application entry point
├── App.tsx                     # Main app component
├── index.css                   # Global styles & Tailwind
├── components/
│   ├── Header.tsx              # App header with title & clear button
│   ├── ChatContainer.tsx       # Main chat wrapper
│   ├── MessageBubble.tsx       # Individual message component
│   ├── MessageInput.tsx        # Message input with send button
│   └── TypingIndicator.tsx     # Bot typing animation
├── services/
│   └── api.ts                  # API client for backend
└── types/
    └── index.ts                # TypeScript interfaces
```

## Components

### Header
- Displays app title and branding
- Clear chat button

### ChatContainer
- Holds all messages
- Auto-scrolls on new messages
- Shows typing indicator when loading

### MessageBubble
- Displays user and bot messages
- Shows booking info card when available
- Animated entry with Framer Motion

### MessageInput
- Text area for user input
- Enter to send (Shift+Enter for new line)
- Disabled state while loading

### TypingIndicator
- Animated dots showing bot is "thinking"

## Styling

The app uses a dark theme with hotel-inspired colors:

```javascript
colors: {
  hotel: {
    dark: '#1a1a2e',    // Main background
    medium: '#16213e',  // Card background
    light: '#0f3460',   // Borders & accents
    accent: '#e94560',  // Highlight color
  }
}
```

## API Integration

The `api.ts` service handles communication with the backend:

```typescript
// Send a chat message
const response = await chatApi.sendMessage('Find hotels in Chennai');

// Clear session (optional)
await chatApi.clearSession(sessionId);
```

## Environment Variables

Create a `.env` file for custom configuration:

```env
VITE_API_URL=http://localhost:8080/api
```

## Development Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Start development server |
| `npm run build` | Build for production |
| `npm run preview` | Preview production build |
| `npm run lint` | Run ESLint |
