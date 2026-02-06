import { motion } from 'framer-motion'
import { Bot, User, Calendar, Users, MapPin, CreditCard, CheckCircle } from 'lucide-react'
import { Message } from '../types'

interface MessageBubbleProps {
  message: Message
}

/**
 * Individual message bubble component with animations.
 */
function MessageBubble({ message }: MessageBubbleProps) {
  const isBot = message.sender === 'bot'

  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.3 }}
      className={`flex gap-3 ${isBot ? 'justify-start' : 'justify-end'}`}
    >
      {isBot && (
        <div className="flex-shrink-0 w-10 h-10 bg-primary-500 rounded-full flex items-center justify-center">
          <Bot className="w-5 h-5 text-white" />
        </div>
      )}

      <div
        className={`max-w-[75%] ${
          isBot
            ? 'bg-hotel-light text-white rounded-2xl rounded-tl-md'
            : 'bg-primary-500 text-white rounded-2xl rounded-tr-md'
        } px-4 py-3 shadow-lg`}
      >
        <p className="text-sm leading-relaxed whitespace-pre-wrap">{message.content}</p>

        {/* Booking Info Card */}
        {message.bookingInfo && (
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.2 }}
            className="mt-3 bg-white/10 rounded-lg p-4 space-y-2"
          >
            <div className="flex items-center gap-2 text-green-400 font-medium">
              <CheckCircle className="w-4 h-4" />
              <span>Booking {message.bookingInfo.status}</span>
            </div>
            <div className="text-xs text-gray-300 space-y-1">
              <div className="flex items-center gap-2">
                <MapPin className="w-3 h-3" />
                <span>{message.bookingInfo.hotelName}, {message.bookingInfo.city}</span>
              </div>
              <div className="flex items-center gap-2">
                <Calendar className="w-3 h-3" />
                <span>{message.bookingInfo.checkIn} → {message.bookingInfo.checkOut}</span>
              </div>
              <div className="flex items-center gap-2">
                <Users className="w-3 h-3" />
                <span>{message.bookingInfo.guests} guest(s)</span>
              </div>
              {message.bookingInfo.totalPrice && (
                <div className="flex items-center gap-2">
                  <CreditCard className="w-3 h-3" />
                  <span>₹{message.bookingInfo.totalPrice.toLocaleString()}</span>
                </div>
              )}
              <div className="pt-2 border-t border-white/20 mt-2">
                <span className="font-mono text-primary-300">
                  ID: {message.bookingInfo.bookingId}
                </span>
              </div>
            </div>
          </motion.div>
        )}

        <p className="text-xs text-gray-400 mt-2">
          {message.timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
        </p>
      </div>

      {!isBot && (
        <div className="flex-shrink-0 w-10 h-10 bg-gray-600 rounded-full flex items-center justify-center">
          <User className="w-5 h-5 text-white" />
        </div>
      )}
    </motion.div>
  )
}

export default MessageBubble
