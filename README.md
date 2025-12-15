# Climate Change Interactive Website

This project is a Spring Boot–based web application for visualizing climate change
and temperature data in the United States. The website can be fully reproduced locally.

---

## Tech Stack

- Java 17
- Spring Boot 3.4.12
- MySQL
- MyBatis
- Maven
- Frontend: HTML / CSS / JavaScript (served by Spring Boot)

---

## Project Structure
```
├── src
│ ├── main
│ │ ├── java
│ │ │ └── com.basic.climate
│ │ └── resources
│ │ ├── static # Frontend pages (HTML/CSS/JS)
│ │ ├── mapper
│ │ └── application.yml
├── database
│ └── climate_db.sql
├── pom.xml
└── README.md
```

---

## Database Setup
### 1. Create Database

Create a MySQL database named `climate_db`:

```sql
CREATE DATABASE climate_db;
```
### 2. Import Data
```
mysql -u root -p climate_db < database/climate_db.sql
```

---

## Configuration
Update database configuration in the following file:

```src/main/resources/application.yml```


Example configuration:

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/climate_db
spring.datasource.username=root
spring.datasource.password=your_password
```
Note: Please update the username and password according to your local MySQL setup.

---
## Run the Project
Ensure that the MySQL service is available and the database has been imported.
If the application starts successfully without database connection errors,
the database connection is correctly configured.


You may start the Spring Boot application using one of the following methods:

- Run the main application class directly in an IDE (e.g., IntelliJ IDEA)
- Or start the application from the project root directory using Maven:

```
mvn spring-boot:run
```
After the application starts, open a browser and visit:
```http://localhost:8080/index```