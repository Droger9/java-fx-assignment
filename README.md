# Foreign Exchange Service

## Overview

This project is a Spring Boot REST API for foreign exchange operations. It provides endpoints for retrieving exchange rates, converting amounts between currencies, and viewing past conversion transactions.

The application uses the Fixer API for exchange rate data and an in-memory H2 database for persistence. It is designed as a small layered backend application with validation, error handling, caching, automated tests, Swagger documentation, and Docker support.

## Features

- Get the current exchange rate between two currencies
- Convert an amount and store the transaction
- View paginated conversion history filtered by transaction ID or date
- Validate currency codes and request payloads
- Return structured error responses with appropriate HTTP status codes
- Cache external exchange rate responses in memory
- Explore the API through Swagger UI

## Tech Stack

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Spring Validation
- Spring Cache
- Springdoc OpenAPI / Swagger UI
- Maven
- Docker
- JUnit and Mockito

## Architecture

The project follows a layered structure:

- Controller layer for handling HTTP requests and responses
- Service layer for business logic
- Client layer for communication with the external exchange-rate provider
- Repository layer for persistence
- DTO, mapper, validation, and specification classes for supporting concerns

## Configuration

The application requires a Fixer API key.

| Variable | Description | Required |
| --- | --- | --- |
| `FIXER_API_KEY` | Fixer API access key | Yes |

Application properties are configured in `src/main/resources/application.properties`.

## Running Locally

Set the Fixer API key:

Windows PowerShell:

```powershell
$env:FIXER_API_KEY="your_api_key_here"
```

Linux/macOS:

```bash
export FIXER_API_KEY=your_api_key_here
```

Run the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The API starts at `http://localhost:8080`.

## Docker

Build the image:

```bash
docker build -t java-fx-assignment .
```

Run the container:

```bash
docker run -p 8080:8080 -e FIXER_API_KEY=your_api_key_here java-fx-assignment
```

## API Overview

Main endpoints:

- `GET /rate`
- `POST /convert`
- `GET /conversions`

Full interactive API documentation is available through Swagger UI:

- `http://localhost:8080/swagger-ui.html`

OpenAPI specification:

- `http://localhost:8080/v3/api-docs`

## Validation and Error Handling

The application validates request bodies, currency codes, and history filters.

Error responses follow a consistent structure:

```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "timestamp": "2026-03-24T10:15:30",
  "errors": {
    "amount": "Amount must be greater than 0"
  }
}
```

Examples of handled errors:

- Invalid request input returns `400 Bad Request`
- Validation failures return `400 Bad Request`
- External exchange-rate provider failures return `502 Bad Gateway`
- Unexpected server errors return `500 Internal Server Error`

## Persistence

The application uses an in-memory H2 database, so stored transactions are lost when the application stops.

H2 console:

- `http://localhost:8080/h2-console`

Default settings:

- JDBC URL: `jdbc:h2:mem:fxdb`
- Username: `sa`
- Password: empty

## Testing

The project includes unit and integration tests.

Run tests with:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Notes

- Exchange rates are fetched from Fixer and cached in memory to reduce repeated external calls
- A valid Fixer API key is required for live exchange-rate data
