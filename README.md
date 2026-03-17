# Foreign Exchange Service

## Overview

This project is a Spring Boot REST API that provides basic foreign exchange functionality. It allows users to retrieve exchange rates, convert amounts between currencies, and store/query past conversion transactions.

The application integrates with the Fixer API for real-time exchange rate data and uses an in-memory H2 database for persistence. It follows a layered architecture and standard REST principles.

---

## Features

* Retrieve exchange rates between two currencies
* Convert amounts using live exchange rates
* Store conversion transactions
* Query conversion history with filtering and pagination
* Input validation and structured error handling
* Interactive API documentation with Swagger
* Docker support for easy setup and execution

---

## Tech Stack

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 In-Memory Database
* Lombok
* Maven
* Docker
* Swagger / OpenAPI
* JUnit & Mockito

---

## Architecture

The application is structured using a layered approach:

* **Controller Layer** – Handles incoming HTTP requests and responses
* **Service Layer** – Contains business logic
* **Client Layer** – Communicates with the external Fixer API
* **Repository Layer** – Manages database operations
* **DTO Layer** – Defines request and response models

### Key Design Choices

* DTOs are used to separate API contracts from database entities
* BigDecimal is used for financial calculations to ensure precision
* UUID is used for transaction identifiers
* External API logic is isolated from core business logic

---

## Environment Variables

| Variable      | Description          | Required |
| ------------- | -------------------- | -------- |
| FIXER_API_KEY | Fixer API access key | Yes      |

---

## Running the Application Locally

### 1. Set the Fixer API key

Windows (PowerShell):

```
$env:FIXER_API_KEY="your_api_key_here"
```

Linux / Mac:

```
export FIXER_API_KEY=your_api_key_here
```

### 2. Run the application

Using Maven wrapper:

```
./mvnw spring-boot:run
```

Or run the `FxApplication` class from your IDE.

The application will start at:

```
http://localhost:8080
```

---

## Running with Docker

### 1. Build the image

```
docker build -t java-fx-assignment .
```

### 2. Run the container

```
docker run -p 8080:8080 -e FIXER_API_KEY=your_api_key_here java-fx-assignment
```

---

## API Endpoints

### Get Exchange Rate

```
GET /rate?from=USD&to=EUR
```

Response:

```
{
  "from": "USD",
  "to": "EUR",
  "rate": 0.871355
}
```

---

### Convert Currency

```
POST /convert
```

Request:

```
{
  "amount": 100,
  "from": "USD",
  "to": "EUR"
}
```

Response:

```
{
  "transactionId": "26df5a85-bb6d-43db-bade-5e6cb8838c5f",
  "from": "USD",
  "to": "EUR",
  "originalAmount": 100,
  "convertedAmount": 87.14,
  "rate": 0.871355
}
```

---

### Get Conversion History

```
GET /conversions
```

Examples:

Filter by date:

```
GET /conversions?date=2026-03-13
```

Filter by transaction ID:

```
GET /conversions?transactionId=26df5a85-bb6d-43db-bade-5e6cb8838c5f
```

Pagination:

```
GET /conversions?date=2026-03-13&page=0&size=5
```

---

## Validation Rules

* amount must be greater than 0
* currency codes must be exactly 3 uppercase letters (e.g., USD, EUR)
* at least one filter (date or transactionId) must be provided

---

## Error Handling

The API returns structured error responses.

Example:

```
{
  "timestamp": "2026-03-17T12:30:00",
  "status": 400,
  "errorCode": "INVALID_REQUEST",
  "message": "Amount must be greater than zero",
  "path": "/convert"
}
```

---

## Testing

The project includes both unit and integration tests.

* Unit tests use Mockito
* Integration tests use Spring Boot and MockMvc

Run tests with:

```
./mvnw test
```

---

## H2 Database Console

```
http://localhost:8080/h2-console
```

Default settings:

* JDBC URL: jdbc:h2:mem:testdb
* Username: sa
* Password: (empty)

---

## API Documentation

Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI spec:

```
http://localhost:8080/v3/api-docs
```

---

## Example Requests

Get exchange rate:

```
curl "http://localhost:8080/rate?from=USD&to=EUR"
```

Convert currency:

```
curl -X POST http://localhost:8080/convert \
  -H "Content-Type: application/json" \
  -d '{"amount":100,"from":"USD","to":"EUR"}'
```

---

## Notes

* The application uses an in-memory database, so data is lost when the app stops
* A valid Fixer API key is required for real exchange rate data
