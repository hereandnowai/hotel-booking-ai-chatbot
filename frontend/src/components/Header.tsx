import { Hotel, MessageSquare, Trash2 } from 'lucide-react'

interface HeaderProps {
  onClearChat: () => void
}

/**
 * Header component with the application title and clear chat action.
 */
function Header({ onClearChat }: HeaderProps) {
  return (
    <header className="bg-hotel-medium border-b border-hotel-light/30 px-6 py-4">
      <div className="max-w-4xl mx-auto flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="bg-primary-500 p-2 rounded-lg">
            <Hotel className="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 className="text-xl font-bold text-white flex items-center gap-2">
              Hotel Booking Assistant
              <MessageSquare className="w-5 h-5 text-primary-400" />
            </h1>
            <p className="text-sm text-gray-400">
              AI-powered concierge at your service
            </p>
          </div>
        </div>
        <button
          onClick={onClearChat}
          className="flex items-center gap-2 px-4 py-2 text-sm text-gray-400 hover:text-white 
                     hover:bg-hotel-light/50 rounded-lg transition-colors"
          title="Clear chat history"
        >
          <Trash2 className="w-4 h-4" />
          <span className="hidden sm:inline">Clear Chat</span>
        </button>
      </div>
    </header>
  )
}

export default Header
