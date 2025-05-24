-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(10) NOT NULL,
    creation_date DATE NOT NULL
);

-- Insert default admin user if not exists
INSERT INTO users (first_name, last_name, username, password, role, creation_date)
SELECT 'Admin', 'User', 'admin', '1234', 'ADMIN', CURRENT_DATE
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
); 