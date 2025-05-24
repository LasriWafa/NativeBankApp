# BankatiWeb Project

A Java web application for banking operations with support for both file-based and database storage.

## Prerequisites

- Java 23 or higher
- Apache Tomcat 10
- MySQL Server
- MySQL Workbench (for database setup)
- Maven

## Project Setup

1. **Database Configuration**
   - Open MySQL Workbench
   - Run the provided SQL query to create the database
   - Update the database connection properties in `src/main/resources/database.properties`:
     ```properties
     username=your_username
     password=your_password
     ```

2. **Project Structure**
   - The project uses a dual-layer DAO (Data Access Object) architecture:
     - File-based DAO layer for file storage operations
     - Database DAO layer for MySQL database operations
   - You can switch between file and database storage by configuring the appropriate DAO implementation

3. **Build and Deploy**
   - Build the project using Maven:
     ```bash
     mvn clean install
     ```
   - Deploy the generated WAR file to Tomcat 10
   - The application will be available at `http://localhost:8080/BankatiWeb`
  
### 🔧 Technologies
- Java EE (Servlets, JSP, JDBC)
- MySQL (latest version)
- IntelliJ IDEA Ultimate
- Apache Tomcat 10

### 🔐 Built-in Users

| Username | Password | Role     |
|----------|----------|----------|
| admin    | 1234     | ADMIN    |

### ⚙️ Important Configuration
To execute this project correctly:
- You must edit the **Tomcat configuration** to provide your own local paths.
- Paths hardcoded for development should be updated based on your local environment.

## Project Architecture

- **DAO Package**: Contains two layers:
  - File-based implementation for local file storage
  - Database implementation for MySQL storage
- **Web Layer**: JSP and Servlet-based web interface
- **Business Logic**: Core banking operations and business rules

## Dependencies

- Jakarta Servlet API 6.0.0
- JSTL 3.0.0
- MySQL Connector 8.0.33
- Lombok 1.18.36

## Notes

- Make sure to configure the correct database credentials in `database.properties`
- The application supports both file-based and database storage - choose the appropriate implementation based on your needs
- Tomcat 10 is required as the application uses Jakarta EE specifications

## License
This project is intended for academic, learning, and demonstration purposes only.

## Author
Wafa Lasri
ouafae.lasri@gmail.com
GitHub: @LasriWafa
