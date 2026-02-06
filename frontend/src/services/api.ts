import axios from 'axios'
import { ChatResponse, ChatRequest } from '../types'

const API_BASE_URL = import.meta.env.VITE_API_URL || '/api'

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 30000, // 30 seconds timeout for AI responses
})

/**
 * Chat API service for communicating with the backend.
 */
export const chatApi = {
  /**
   * Sends a user message to the chatbot and returns the response.
   */
  async sendMessage(message: string, sessionId?: string): Promise<ChatResponse> {
    const request: ChatRequest = { message, sessionId }
    const response = await apiClient.post<ChatResponse>('/chat', request)
    return response.data
  },

  /**
   * Clears the chat session history on the server.
   */
  async clearSession(sessionId: string): Promise<void> {
    await apiClient.delete(`/chat/session/${sessionId}`)
  },
}

/**
 * Health check API.
 */
export const healthApi = {
  /**
   * Checks if the backend is available.
   */
  async check(): Promise<boolean> {
    try {
      await apiClient.get('/health')
      return true
    } catch {
      return false
    }
  },
}
