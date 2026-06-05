# Employee Performance Tracker

A Spring Boot REST API for tracking employee performance reviews, goals, and cycle summaries.

---

## Tech Stack
- Java 17 + Spring Boot 3.2
- Spring Data JPA + Hibernate
- H2 in-memory database
- Bean Validation (Jakarta)
- Lombok

---

## Project Setup — Step by Step

### Prerequisites
- Java 17+ installed (`java -version`)
- Maven 3.8+ installed (`mvn -version`)

### Step 1 — Clone / unzip the project
```bash
cd performance-tracker
```

### Step 2 — Build the project
```bash
mvn clean package -DskipTests
```

### Step 3 — Run the application
```bash
mvn spring-boot:run
```
The server starts on **http://localhost:8080**

### Step 4 — (Optional) Open H2 Console
Visit: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:performancedb`
- Username: `amanverma`
- Password: *secret*

---
