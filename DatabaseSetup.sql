-- Create the database
CREATE DATABASE IF NOT EXISTS myBankatiDB;
USE myBankatiDB;

-- Create users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    creation_date DATE NOT NULL
);

-- Create accounts table
CREATE TABLE accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    balance DOUBLE NOT NULL DEFAULT 0.0,
    creation_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create credit_requests table
CREATE TABLE credit_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    request_date DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    rejection_reason VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create currencies table (for exchange rates)
CREATE TABLE currencies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    exchange_rate DOUBLE NOT NULL,
    last_updated DATETIME NOT NULL
);

-- Insert default currencies
INSERT INTO currencies (code, name, exchange_rate, last_updated) VALUES
('MAD', 'Moroccan Dirham', 1.0, NOW()),
('USD', 'US Dollar', 0.10, NOW()),
('EUR', 'Euro', 0.092, NOW()),
('GBP', 'British Pound', 0.079, NOW());

INSERT INTO users (first_name, last_name, username, password, role, creation_date)
SELECT 'Wafa', 'Lasri', 'admin', '1234', 'ADMIN', CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);

select * from users;
select * from accounts;
select * from credit_requests;
select * from currencies;