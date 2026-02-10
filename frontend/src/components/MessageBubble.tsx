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
        <div className="flex-shrink-0 w-10 h-10 border-2 border-hnai-primary rounded-full overflow-hidden flex items-center justify-center bg-hnai-secondary">
          <img 
            src="https://raw.githubusercontent.com/hereandnowai/images/refs/heads/main/logos/caramel-face.jpeg" 
            alt="Caramel" 
            className="w-full h-full object-cover"
          />
        </div>
      )}

      <div
        className={`max-w-[75%] shadow-xl ${
          isBot
            ? 'bg-hnai-secondary text-white rounded-2xl rounded-tl-md border border-hnai-primary/20'
            : 'bg-hnai-primary text-hnai-secondary font-medium rounded-2xl rounded-tr-md'
        } px-4 py-3`}
      >
        <p className="text-sm leading-relaxed whitespace-pre-wrap">{message.content}</p>

        {/* Booking Info Card */}
        {message.bookingInfo && (
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: 0.2 }}
            className={`mt-3 rounded-lg p-4 space-y-2 ${
              isBot ? 'bg-white/10' : 'bg-hnai-secondary/10'
            }`}
          >
            <div className={`flex items-center gap-2 font-bold ${
              isBot ? 'text-hnai-primary' : 'text-hnai-secondary'
            }`}>
              <CheckCircle className="w-4 h-4" />
              <span>Booking {message.bookingInfo.status}</span>
            </div>
            <div className={`text-xs space-y-1 ${
              isBot ? 'text-gray-300' : 'text-hnai-secondary/80'
            }`}>
              <div className="flex items-center gap-2">
                <MapPin className="w-3 h-3" />
                <span className="font-semibold">{message.bookingInfo.hotelName}, {message.bookingInfo.city}</span>
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
                  <span className="font-bold text-base">₹{message.bookingInfo.totalPrice.toLocaleString()}</span>
                </div>
              )}
              <div className={`pt-2 border-t mt-2 ${
                isBot ? 'border-white/20' : 'border-hnai-secondary/20'
              }`}>
                <span className={`font-mono text-xs ${
                  isBot ? 'text-hnai-primary/80' : 'text-hnai-secondary'
                }`}>
                  Ref: {message.bookingInfo.bookingId}
                </span>
              </div>
            </div>
          </motion.div>
        )}

        <p className={`text-[10px] mt-2 font-medium ${
          isBot ? 'text-hnai-primary/50' : 'text-hnai-secondary/50'
        }`}>
          {message.timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
        </p>
      </div>

      {!isBot && (
        <div className="flex-shrink-0 w-10 h-10 bg-hnai-secondary rounded-full flex items-center justify-center border-2 border-hnai-primary">
          <User className="w-5 h-5 text-hnai-primary" />
        </div>
      )}
    </motion.div>
  )
}

export default MessageBubble
