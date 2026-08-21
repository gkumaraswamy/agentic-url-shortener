# Agentic URL Shortener

Spring Boot 3 service that shortens URLs with Base62 encoding, SSRF checks, click analytics, and a small agentic SDLC orchestrator used to simulate gated delivery (requirements → architecture → tests → security → human approval).

## Requirements

- JDK 21
- Maven 3.9+

## Build

```bash
mvn clean package
```

The executable jar is `target/agentic-url-shortener-1.0.0.jar`.

## Run

```bash
mvn spring-boot:run
```

Or:

```bash
java -jar target/agentic-url-shortener-1.0.0.jar
```

The app listens on **http://localhost:8080**. H2 console is at `/h2-console` (JDBC URL `jdbc:h2:mem:shortenerdb`, user `sa`, empty password). Default link TTL is 30 days.

## API

### Shorten a URL

`POST /api/v1/shorten`

```json
{
  "originalUrl": "https://www.schwab.com/investment-products",
  "customAlias": "invest",
  "ttlDays": 30
}
```

`customAlias` and `ttlDays` are optional. Response includes `shortCode`, `shortUrl`, `originalUrl`, `createdAt`, and `expiresAt`.

### Redirect

`GET /{shortCode}` — `302` to the original URL, and records a click.

### Analytics

`GET /api/v1/analytics/{shortCode}` — total clicks plus recent click metadata.

## Security

`SecurityValidator` allows only `http`/`https` and blocks internal targets such as `localhost`, `127.0.0.1`, `0.0.0.0`, `internal.corp`, and cloud metadata `169.254.169.254`.

## Tests

```bash
mvn test
```

- `UrlShortenerServiceTest` — shorten/resolve and SSRF rejection
- `OrchestrationEngineTest` — happy-path deploy vs HITL block

## Layout

```
src/main/java/com/schwab/agentic/
  Application.java
  orchestrator/     SDLC agents and OrchestrationEngine
  service/          REST API, persistence, Base62, SSRF checks
docs/
  ARCHITECTURE.md
  SCENARIOS.md
```
