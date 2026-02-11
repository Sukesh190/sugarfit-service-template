# Sugarfit Service Template

A Spring Boot starter template for building Sugarfit platform microservices.

## How to Run

### Prerequisites

- Java 17+
- Maven 3.9+ (or use the included Maven wrapper)

### Run Locally

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The service starts on `http://localhost:8080` with the `dev` profile by default.

### Run with Production Profile

```bash
# Linux / macOS
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

# Windows
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

### Run Tests

```bash
# Linux / macOS
./mvnw test

# Windows
mvnw.cmd test
```

### Build and Run as JAR

**Before building for production**, update the allowed CORS origins in `src/main/resources/application-prod.properties`:
```properties
cors.allowed-origins=https://your-domain.com
```

```bash
# Linux / macOS
./mvnw clean package

# Windows
mvnw.cmd clean package

# Run with production profile
java -jar target/sugarfit-service-template-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/health` | Health check |
| POST | `/example` | Data processor |

**POST /example**

```bash
curl -X POST http://localhost:8080/example \
  -H "Content-Type: application/json" \
  -d '{"userId": "123", "value": 42}'
```

```json
{
  "status": "SUCCESS",
  "requestId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

## Design Decisions

### Layered Architecture

Code is split into clear layers so that any new engineer knows where things go:

- `controller/` — Handles HTTP requests and validation.
- `service/` — Business logic interfaces.
- `service/impl/` — Business logic implementations.
- `dto/` — Request, response, and error objects.
- `exception/` — Central place for all error handling.
- `web/` — Filters and configs (CORS, request ID).
- `constants/` — Shared constants used across the app.

Each layer can be tested independently.

### Request ID Tracking

A servlet filter generates a unique UUID for every request and stores it in the logging context (MDC). This ID shows up in every log line for that request, making it easy to trace a single request through the logs when debugging.

### Error Handling

One global exception handler catches all errors and returns clean, consistent JSON responses. Clients never see raw stack traces. Validation errors return field-level details so the caller knows exactly what went wrong.

All errors are logged — `warn` for client mistakes (bad input, wrong method), `error` with full stack trace for unexpected server failures.

### Environment Profiles

Two profiles out of the box:

- **dev** — Debug logging, all CORS origins allowed, readable console output.
- **prod** — Tomcat thread pool tuned (500 connections, 100 threads), CORS locked to specific origins, JSON log output for tools like ELK or Datadog.

CORS origins are configured in properties files, so you can change them per environment without touching code.

### Logging

- In dev: human-readable colored console output with request ID.
- In prod: structured JSON logs with fields like `requestId`, `traceId`, `spanId` for log aggregation and tracing.

### Graceful Shutdown

The server waits up to 30 seconds for in-flight requests to finish before shutting down. This avoids dropping requests during deployments.

### Input Validation

Request DTOs use Bean Validation annotations (`@NotBlank`, `@NotNull`). Invalid requests are caught automatically and returned as structured error responses with per-field messages.

## Assumptions

- **No database** — This template covers the web layer only. Database setup would be added per service as needed.
- **No auth** — Security is left out intentionally. Each service would plug into the org's auth system.
- **Embedded Tomcat** — Uses the default embedded server with production thread pool tuning in the prod profile.
- **Actuator for health** — Uses Spring Boot Actuator's built-in `/health` endpoint instead of a custom one.

## Future Roadmap

- **API Versioning** — Add `/v1/example/` base path for safe API evolution.
- **Rate Limiting** — Protect endpoints from abuse under high traffic.
- **Dockerfile** — Multi-stage Docker build for container deployments.
- **CI/CD Pipeline** — GitHub Actions for automated build, test, and publish.
- **Distributed Tracing** — Micrometer Tracing with Zipkin/Jaeger for cross-service request tracing.
- **API Documentation** — OpenAPI/Swagger for auto-generated, interactive API docs.
