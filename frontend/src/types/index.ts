/**
 * Represents a chat message in the conversation.
 */
export interface Message {
  id: string
  content: string
  sender: 'user' | 'bot'
  timestamp: Date
  bookingInfo?: BookingInfo
}

/**
 * Booking information returned from the backend.
 */
export interface BookingInfo {
  bookingId: string
  hotelName: string
  city: string
  checkIn: string
  checkOut: string
  guests: number
  status: BookingStatus
  totalPrice?: number
}

/**
 * Possible booking status values.
 */
export type BookingStatus = 'CONFIRMED' | 'CANCELLED' | 'MODIFIED'

/**
 * Chat API response structure.
 */
export interface ChatResponse {
  message: string
  bookingInfo?: BookingInfo
  hotels?: HotelInfo[]
}

/**
 * Hotel information for search results.
 */
export interface HotelInfo {
  id: string
  name: string
  city: string
  pricePerNight: number
  roomType: string
  available: boolean
}

/**
 * Request payload for chat messages.
 */
export interface ChatRequest {
  message: string
  sessionId?: string
}
