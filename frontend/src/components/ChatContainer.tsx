import { useRef, useEffect } from 'react'
import { Message } from '../types'
import MessageBubble from './MessageBubble'
import MessageInput from './MessageInput'
import TypingIndicator from './TypingIndicator'

interface ChatContainerProps {
  messages: Message[]
  onSendMessage: (message: string) => void
  isLoading: boolean
}

/**
 * Main chat container that holds all messages and the input field.
 */
function ChatContainer({ messages, onSendMessage, isLoading }: ChatContainerProps) {
  const messagesEndRef = useRef<HTMLDivElement>(null)

  // Auto-scroll to bottom when new messages arrive
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages, isLoading])

  return (
    <div className="w-full max-w-4xl bg-hnai-secondary rounded-2xl shadow-2xl flex flex-col h-[calc(100vh-160px)] border border-hnai-primary/10">
      {/* Messages area */}
      <div className="flex-1 overflow-y-auto p-6 space-y-4">
        {messages.map((message) => (
          <MessageBubble key={message.id} message={message} />
        ))}
        {isLoading && <TypingIndicator />}
        <div ref={messagesEndRef} />
      </div>

      {/* Input area */}
      <div className="border-t border-hnai-primary/20 p-4">
        <MessageInput onSend={onSendMessage} disabled={isLoading} />
      </div>
    </div>
  )
}

export default ChatContainer
