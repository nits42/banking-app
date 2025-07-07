# Banking App

## Overview

The **Banking App** is a comprehensive core banking system designed to manage customer accounts, transactions, and card
services. It provides features such as account creation, fund transfers, balance checking, transaction history, and card
management. The application is built using modern technologies and follows a microservices-based architecture.

---

## Architecture

The application follows a **microservices architecture** with the following components:

- **Discovery Server**: Service registry for microservices.
- **API Gateway**: Centralized entry point for routing requests to microservices.
- **User Service**: Manages user information and operations.
- **Auth Service**: Handles user's authentication and authorization.
- **Core Banking Service**: Handles account operations, transactions, and card management.
- **ATM Service**: Handles all ATM-related operations.
- **Database**: PostgreSQL for persistent storage.

![architecture](https://github.com/user-attachments/assets/67327d6d-40c1-4dec-a001-ac252187576b)

---

## Tech Stack

### Backend

- **Java**: Programming language.
- **Spring Boot**: Framework for building microservices.
- **Spring Data JPA**: For database interactions.
- **Spring Cloud**: For service discovery and configuration.
- **Hibernate**: ORM for database operations.
- **Maven**: Build automation tool.

### Database

- **PostgreSQL**: Relational database for persistent storage used in all other services.
- **MongoDB**: NoSQL database for storing Files, used in file-storage-service.

### API Documentation

- **Swagger/OpenAPI**: For documenting and testing APIs.

### Security

- **Spring Security**: For authentication and authorization.
- **JWT**: For secure token-based authentication.

### Logging & Monitoring

- **SLF4J with Logback**: For logging.

### Containerization & Deployment

- **Docker**: For containerizing microservices.

---

## Features

1. **Account Management**:

- Open new accounts.
- Check account balance.
- Get account details by account number.
- Count active/inactive accounts.

2. **Transaction Management**:

- Fund transfers between accounts.
- Mini statement (last 10 transactions).
- Detailed statement for a specific date range.

3. **Card Management**:

- Add new debit cards.
- Block debit/credit cards.
- Change card PIN.

4. **Internationalization (i18n)**:

- Supports multiple languages for user messages.

5. **Error Handling**:

- Custom exceptions for better error reporting.

---

## Configuration

### Application Properties

The application uses `application.yml` for configuration. Below is an example:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/banking_app
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

jwt:
  secret: your-secret-key
  expiration: 3600000

Sample
"dateOfBirth": "1998-01-31",
    
