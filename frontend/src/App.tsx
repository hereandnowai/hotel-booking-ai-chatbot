import { useState, useCallback } from 'react'
import ChatContainer from './components/ChatContainer'
import Header from './components/Header'
import { Message } from './types'
import { chatApi } from './services/api'

/**
 * Main application component for the Hotel Booking Chatbot.
 * Manages the chat state and coordinates between UI components.
 */
function App() {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: '1',
      content: "Hello! I'm your AI hotel concierge. I can help you search for hotels, make bookings, modify reservations, or cancel them. How can I assist you today?",
      sender: 'bot',
      timestamp: new Date(),
    },
  ])
  const [isLoading, setIsLoading] = useState(false)

  const handleSendMessage = useCallback(async (content: string) => {
    if (!content.trim()) return

    // Add user message
    const userMessage: Message = {
      id: Date.now().toString(),
      content,
      sender: 'user',
      timestamp: new Date(),
    }
    setMessages(prev => [...prev, userMessage])
    setIsLoading(true)

    try {
      // Send to backend and get response
      const response = await chatApi.sendMessage(content)
      
      const botMessage: Message = {
        id: (Date.now() + 1).toString(),
        content: response.message,
        sender: 'bot',
        timestamp: new Date(),
        bookingInfo: response.bookingInfo,
      }
      setMessages(prev => [...prev, botMessage])
    } catch (error) {
      console.error('Failed to send message:', error)
      const errorMessage: Message = {
        id: (Date.now() + 1).toString(),
        content: "I'm sorry, I encountered an error processing your request. Please try again.",
        sender: 'bot',
        timestamp: new Date(),
      }
      setMessages(prev => [...prev, errorMessage])
    } finally {
      setIsLoading(false)
    }
  }, [])

  const handleClearChat = useCallback(() => {
    setMessages([
      {
        id: '1',
        content: "Hello! I'm your AI hotel concierge. I can help you search for hotels, make bookings, modify reservations, or cancel them. How can I assist you today?",
        sender: 'bot',
        timestamp: new Date(),
      },
    ])
  }, [])

  return (
    <div className="min-h-screen bg-hotel-dark flex flex-col">
      <Header onClearChat={handleClearChat} />
      <main className="flex-1 flex justify-center px-4 py-6">
        <ChatContainer
          messages={messages}
          onSendMessage={handleSendMessage}
          isLoading={isLoading}
        />
      </main>
    </div>
  )
}

export default App
