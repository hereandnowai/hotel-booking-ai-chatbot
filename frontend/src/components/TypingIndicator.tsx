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
      <div className="flex-shrink-0 w-10 h-10 border-2 border-hnai-primary rounded-full overflow-hidden flex items-center justify-center bg-hnai-secondary">
        <img 
          src="https://raw.githubusercontent.com/hereandnowai/images/refs/heads/main/logos/caramel-face.jpeg" 
          alt="Caramel" 
          className="w-full h-full object-cover"
        />
      </div>
      <div className="bg-hnai-secondary border border-hnai-primary/20 rounded-2xl rounded-tl-md px-4 py-3 shadow-lg">
        <div className="flex items-center gap-1">
          <span className="w-2 h-2 bg-hnai-primary/50 rounded-full typing-dot" />
          <span className="w-2 h-2 bg-hnai-primary/50 rounded-full typing-dot" />
          <span className="w-2 h-2 bg-hnai-primary/50 rounded-full typing-dot" />
        </div>
      </div>
    </motion.div>
  )
}

export default TypingIndicator
