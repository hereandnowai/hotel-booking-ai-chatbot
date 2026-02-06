-- Database initialization script for Hotel Booking Chatbot
-- Run this script to create the database and initial schema

-- Create database (run as postgres superuser)
-- CREATE DATABASE hotel_chatbot;

-- Connect to hotel_chatbot database and run the following:

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hotels table
CREATE TABLE IF NOT EXISTS hotels (
    hotel_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address TEXT,
    price_per_night INTEGER NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    availability BOOLEAN DEFAULT TRUE,
    rating DECIMAL(2,1),
    amenities TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    booking_reference VARCHAR(20) UNIQUE NOT NULL,
    user_id UUID REFERENCES users(user_id) ON DELETE SET NULL,
    hotel_id UUID REFERENCES hotels(hotel_id) ON DELETE SET NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    guests INTEGER NOT NULL DEFAULT 1,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    total_price INTEGER,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT valid_dates CHECK (check_out > check_in),
    CONSTRAINT valid_status CHECK (status IN ('CONFIRMED', 'CANCELLED', 'MODIFIED'))
);

-- Create indexes for common queries
CREATE INDEX IF NOT EXISTS idx_hotels_city ON hotels(city);
CREATE INDEX IF NOT EXISTS idx_hotels_availability ON hotels(availability);
CREATE INDEX IF NOT EXISTS idx_hotels_price ON hotels(price_per_night);
CREATE INDEX IF NOT EXISTS idx_bookings_user ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_bookings_status ON bookings(status);
CREATE INDEX IF NOT EXISTS idx_bookings_reference ON bookings(booking_reference);

-- Full text search index for hotel location queries
CREATE INDEX IF NOT EXISTS idx_hotels_city_search ON hotels USING gin(to_tsvector('english', city || ' ' || COALESCE(address, '')));
