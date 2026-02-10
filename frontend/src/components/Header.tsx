import { Hotel, MessageSquare, Trash2 } from 'lucide-react'

interface HeaderProps {
  onClearChat: () => void
}

/**
 * Header component with the application title and clear chat action.
 */
function Header({ onClearChat }: HeaderProps) {
  return (
    <header className="bg-hnai-secondary border-b border-hnai-primary/20 px-6 py-4">
      <div className="max-w-4xl mx-auto flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="bg-hnai-primary p-2 rounded-lg">
            <img 
              src="https://raw.githubusercontent.com/hereandnowai/images/refs/heads/main/logos/logo-of-here-and-now-ai.png" 
              alt="HNAI Logo" 
              className="w-8 h-8 object-contain"
            />
          </div>
          <div>
            <h1 className="text-xl font-bold text-hnai-primary flex items-center gap-2">
              Hotel Assistant
              <MessageSquare className="w-5 h-5 text-hnai-primary" />
            </h1>
            <p className="text-xs text-hnai-primary/70 font-medium">
              AI is Good | Powered by HERE AND NOW AI
            </p>
          </div>
        </div>
        <button
          onClick={onClearChat}
          className="flex items-center gap-2 px-4 py-2 text-sm text-hnai-primary/80 hover:text-hnai-primary 
                     hover:bg-hnai-primary/10 rounded-lg transition-colors border border-hnai-primary/20"
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
