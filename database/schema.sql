-- ============================================================
--  DriveEase Vehicle Rental System — MySQL Schema
--  Run this file once to set up the database
-- ============================================================

CREATE DATABASE IF NOT EXISTS driveease_db;
USE driveease_db;

-- ──────────────────────────────────────────────
-- USERS TABLE
-- ──────────────────────────────────────────────
CREATE TABLE users (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(100) NOT NULL UNIQUE,
    email        VARCHAR(150) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,   -- BCrypt hashed
    role         ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ──────────────────────────────────────────────
-- VEHICLES TABLE
-- ──────────────────────────────────────────────
CREATE TABLE vehicles (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(150) NOT NULL,
    type         VARCHAR(50)  NOT NULL,   -- Sedan, SUV, Bike, etc.
    emoji        VARCHAR(10)  NOT NULL,
    price_per_day DECIMAL(10,2) NOT NULL,
    seats        INT          NOT NULL,
    fuel_type    VARCHAR(50)  NOT NULL,   -- Petrol, Diesel, Electric
    available    BOOLEAN      DEFAULT TRUE,
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ──────────────────────────────────────────────
-- BOOKINGS TABLE
-- ──────────────────────────────────────────────
CREATE TABLE bookings (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_ref  VARCHAR(20)  NOT NULL UNIQUE,
    user_id      BIGINT       NOT NULL,
    vehicle_id   BIGINT       NOT NULL,
    num_days     INT          NOT NULL,
    base_amount  DECIMAL(10,2) NOT NULL,
    tax_amount   DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status       ENUM('ACTIVE','COMPLETED','CANCELLED') DEFAULT 'ACTIVE',
    booking_date DATE         NOT NULL,
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_user    FOREIGN KEY (user_id)    REFERENCES users(id),
    CONSTRAINT fk_booking_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

-- ──────────────────────────────────────────────
-- SEED DATA
-- ──────────────────────────────────────────────

-- Default admin user  (password: admin123)
INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@driveease.com',
 '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
 'ADMIN');

-- Sample vehicles
INSERT INTO vehicles (name, type, emoji, price_per_day, seats, fuel_type) VALUES
('Toyota Camry',    'Sedan',    '🚗', 2500.00, 5, 'Petrol'),
('Honda CR-V',      'SUV',      '🚙', 3800.00, 7, 'Diesel'),
('Maruti Swift',    'Hatchback','🚕', 1500.00, 5, 'Petrol'),
('BMW 3 Series',    'Luxury',   '🏎️', 7000.00, 5, 'Petrol'),
('Mahindra Bolero', 'MUV',      '🛻', 3200.00, 9, 'Diesel'),
('KTM Duke 390',    'Bike',     '🏍️',  900.00, 1, 'Petrol');
