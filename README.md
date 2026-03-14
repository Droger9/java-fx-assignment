# Foreign Exchange Service

A small Spring Boot REST API that provides currency exchange and conversion.

The service integrates with the **Fixer API** to retrieve live exchange rates and stores conversion transactions in an **H2 in-memory database**.

Main features:

- Retrieve exchange rates between currencies
- Convert amounts between currencies
- Store conversion transactions
- Query conversion history with filters and pagination

---

## Tech Stack

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 In-Memory Database
- Lombok
- Maven
- Docker
- Swagger / OpenAPI
- JUnit & Mockito

---

## Running the Application Locally

### 1. Set the Fixer API key

The application requires a Fixer API key.

Set an environment variable:

Windows (PowerShell)

$env:FIXER_API_KEY="your_api_key_here"

Linux / Mac

export FIXER_API_KEY=your_api_key_here

---

### 2. Run the application

Using Maven wrapper:

./mvnw spring-boot:run

Or run the FxApplication class directly from your IDE.

The application will start on:

http://localhost:8080

## Running with Docker

### 1. Build the Docker image

docker build -t java-fx-assignment .

### 2. Run the container

docker run -p 8080:8080 -e FIXER_API_KEY=your_api_key_here java-fx-assignment

The API will be available at:

http://localhost:8080

---

## API Endpoints

### Get Exchange Rate

GET /rate

Example request:

GET /rate?from=USD&to=EUR

Example response:

{
  "from": "USD",
  "to": "EUR",
  "rate": 0.871355
}

---

### Convert Currency

POST /convert

Example request:

{
  "amount": 100,
  "from": "USD",
  "to": "EUR"
}

Example response:

{
  "transactionId": "26df5a85-bb6d-43db-bade-5e6cb8838c5f",
  "from": "USD",
  "to": "EUR",
  "originalAmount": 100,
  "convertedAmount": 87.14,
  "rate": 0.871355
}

---

### Get Conversion History

GET /conversions

Filter by date:

GET /conversions?date=2026-03-13

Filter by transaction id:

GET /conversions?transactionId=<uuid>

Pagination example:

GET /conversions?date=2026-03-13&page=0&size=5

---

## API Documentation

Swagger UI is available when the application is running:

http://localhost:8080/swagger-ui.html

The OpenAPI specification can be accessed at:

http://localhost:8080/v3/api-docs