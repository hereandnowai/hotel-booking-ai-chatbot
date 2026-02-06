import { motion } from 'framer-motion'
import { Bot } from 'lucide-react'

/**
 * Typing indicator shown when the bot is processing a response.
 */
function TypingIndicator() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 10 }}
      animate={{ opacity: 1, y: 0 }}
      className="flex gap-3 justify-start"
    >
      <div className="flex-shrink-0 w-10 h-10 bg-primary-500 rounded-full flex items-center justify-center">
        <Bot className="w-5 h-5 text-white" />
      </div>
      <div className="bg-hotel-light rounded-2xl rounded-tl-md px-4 py-3 shadow-lg">
        <div className="flex items-center gap-1">
          <span className="w-2 h-2 bg-gray-400 rounded-full typing-dot" />
          <span className="w-2 h-2 bg-gray-400 rounded-full typing-dot" />
          <span className="w-2 h-2 bg-gray-400 rounded-full typing-dot" />
        </div>
      </div>
    </motion.div>
  )
}

export default TypingIndicator
