# Student Information System

A comprehensive Java web application for managing student information with a modern HTML5 frontend.

## Features

- **Student Management**: Add, view, update, delete, and search students
- **Course Reports**: Generate graphical reports of student distribution by course
- **RESTful API**: JSON-based API endpoints for all operations
- **Responsive UI**: Bootstrap 5 based modern interface
- **Database Integration**: MySQL database with automatic table creation

## Technology Stack

- **Backend**: Java Servlets, JDBC
- **Frontend**: HTML5, Bootstrap 5, JavaScript, Chart.js
- **Database**: MySQL
- **Build Tool**: Maven
- **Server**: Apache Tomcat (via Maven plugin)

## Prerequisites

- Java 11 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Apache Tomcat (optional, Maven plugin included)

## Setup Instructions

### 1. Database Setup

1. Start MySQL service
2. Run the database creation script:
   ```sql
   mysql -u root -p < create_db.sql
   ```
   Or manually execute:
   ```sql
   CREATE DATABASE student_db;
   ```

### 2. Project Setup

1. Clone or download the project
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean install
   ```

### 3. Configuration

Update database credentials in `src/main/java/config/DatabaseConfig.java` if needed:
```java
private static final String URL = "jdbc:mysql://localhost:3306/student_db?useSSL=false&serverTimezone=UTC";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

### 4. Run the Application

Start the application using Maven Tomcat plugin:
```bash
mvn tomcat7:run
```

The application will be available at: http://localhost:8080/student-system/

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/students` | Get all students |
| GET | `/students/{rollNo}` | Get student by roll number |
| GET | `/students/search?q={term}` | Search students by roll number or name |
| GET | `/students/report` | Get course-wise student count |
| POST | `/students` | Add new student |
| PUT | `/students` | Update existing student |
| DELETE | `/students/{rollNo}` | Delete student |

## Project Structure

```
student-system/
├── src/main/java/
│   ├── config/DatabaseConfig.java    # Database connection management
│   ├── models/Student.java           # Student entity model
│   ├── dao/StudentDAO.java           # Data access operations
│   └── controllers/StudentServlet.java # REST API controller
├── src/main/webapp/
│   ├── index.html                    # Main application interface
│   └── WEB-INF/web.xml               # Web application configuration
├── pom.xml                           # Maven configuration
├── create_db.sql                     # Database setup script
└── README.md                         # This file
```

## Usage

1. Open http://localhost:8080/student-system/ in your browser
2. Use the sidebar navigation to:
   - View dashboard with statistics
   - Add new students
   - View all students in a table
   - Search students by roll number or name
   - Generate course-wise reports

## Development

### Adding New Features

1. Update the `StudentDAO` class for new database operations
2. Add new endpoints in `StudentServlet`
3. Update the frontend JavaScript and HTML as needed

### Building for Production

```bash
mvn clean package
```

This creates a WAR file in the `target/` directory that can be deployed to any servlet container.

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running
- Check database credentials in `DatabaseConfig.java`
- Verify database `student_db` exists

### Port Conflicts
- Change the port in `pom.xml` if 8080 is in use:
  ```xml
  <configuration>
      <port>9090</port>
  </configuration>
  ```

### Build Errors
- Ensure Java 11+ is installed
- Run `mvn clean` before rebuilding

## License

This project is for educational purposes.

## Contributing

Feel free to submit issues and enhancement requests.
