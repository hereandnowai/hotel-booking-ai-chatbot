import { useState, useCallback, KeyboardEvent } from 'react'
import { Send } from 'lucide-react'

interface MessageInputProps {
  onSend: (message: string) => void
  disabled?: boolean
}

/**
 * Message input component with send button.
 */
function MessageInput({ onSend, disabled = false }: MessageInputProps) {
  const [message, setMessage] = useState('')

  const handleSend = useCallback(() => {
    if (message.trim() && !disabled) {
      onSend(message.trim())
      setMessage('')
    }
  }, [message, onSend, disabled])

  const handleKeyDown = useCallback(
    (e: KeyboardEvent<HTMLTextAreaElement>) => {
      if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault()
        handleSend()
      }
    },
    [handleSend]
  )

  return (
    <div className="flex items-end gap-3">
      <textarea
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Type your message... (e.g., 'Book a hotel in Chennai for 2 guests')"
        disabled={disabled}
        rows={1}
        className="flex-1 resize-none bg-hotel-dark border border-hotel-light/50 rounded-xl px-4 py-3 
                   text-white placeholder-gray-500 focus:outline-none focus:border-primary-500 
                   focus:ring-2 focus:ring-primary-500/20 transition-all disabled:opacity-50
                   min-h-[48px] max-h-[120px]"
        style={{ overflow: 'auto' }}
      />
      <button
        onClick={handleSend}
        disabled={disabled || !message.trim()}
        className="flex-shrink-0 bg-primary-500 hover:bg-primary-600 disabled:bg-gray-600 
                   disabled:cursor-not-allowed text-white p-3 rounded-xl transition-colors
                   shadow-lg hover:shadow-primary-500/25"
        title="Send message"
      >
        <Send className="w-5 h-5" />
      </button>
    </div>
  )
}

export default MessageInput
