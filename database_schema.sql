-- Blood Bank Management System Database Schema
-- Drop database if exists and create fresh
DROP DATABASE IF EXISTS blood_bank;
CREATE DATABASE blood_bank;
USE blood_bank;

-- Admin table for admin login
CREATE TABLE admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Donor table
CREATE TABLE donor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    blood_group VARCHAR(5) NOT NULL,
    contact VARCHAR(15) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Users table (blood receivers)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(15) NOT NULL
);

-- Blood stock table
CREATE TABLE blood_stock (
    blood_group VARCHAR(5) PRIMARY KEY,
    units INT DEFAULT 0
);

-- Blood issue table
CREATE TABLE blood_issue (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_name VARCHAR(100) NOT NULL,
    blood_group VARCHAR(5) NOT NULL,
    units INT NOT NULL,
    receiver_address TEXT NOT NULL,
    issue_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert default admin account
INSERT INTO admin (username, password) VALUES ('admin', 'admin123');

-- Initialize blood stock with all blood groups
INSERT INTO blood_stock (blood_group, units) VALUES 
    ('A+', 0),
    ('A-', 0),
    ('B+', 0),
    ('B-', 0),
    ('AB+', 0),
    ('AB-', 0),
    ('O+', 0),
    ('O-', 0);

-- Insert sample donors for testing
INSERT INTO donor (name, age, gender, blood_group, contact, password) VALUES
    ('John Doe', 25, 'Male', 'O+', '9876543210', 'donor123'),
    ('Jane Smith', 30, 'Female', 'A+', '9876543211', 'donor123'),
    ('Mike Johnson', 28, 'Male', 'B+', '9876543212', 'donor123');

-- Insert sample user for testing
INSERT INTO users (username, password, name, contact) VALUES
    ('user1', 'user123', 'Alice Brown', '9876543220');
