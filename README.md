# NXT STS

[![Build](https://github.com/nxtgrid/nxt-sts/actions/workflows/build.yml/badge.svg)](https://github.com/nxtgrid/nxt-sts/actions/workflows/build.yml)
[![License: AGPL v3](https://img.shields.io/badge/License-AGPL_v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

NXT STS is a lightweight Spring Boot microservice that generates prepayment tokens compliant with the **IEC 62055-41 (STS)** standard. It is used in production to power prepaid utility metering workflows and exposes a simple REST API for token generation.

> This project is a derivative work of [NectarAPI/tokens-service](https://github.com/NectarAPI/tokens-service), which is licensed under AGPL-3.0. See [NOTICE](NOTICE) for full attribution.

---

## Supported Token Types

Four token types are exposed via `POST /token` today. The underlying STS library implements many more; see **[docs/capabilities.md](docs/capabilities.md)** for the full matrix.

| API `type` | STS class / subclass | Description | REST API |
|---|---|---|:---:|
| `TOP_UP` | 0 / 0 | Transfer electricity credit (kWh) | **Yes** |
| `CLEAR_CREDIT` | 2 / 1 | Clear existing credit on meter | **Yes** |
| `CLEAR_TAMPER` | 2 / 5 | Clear tamper condition | **Yes** |
| `SET_POWER_LIMIT` | 2 / 0 | Set maximum power limit | **Yes** |

Tokens are generated using the **Standard Transfer Algorithm (STA / EA07)** via the [Bouncy Castle](https://www.bouncycastle.org/) cryptographic library.

To add a new token type, see [Adding a token type](CONTRIBUTING.md#adding-a-token-type) in `CONTRIBUTING.md`.

---

## Architecture

```
HTTP client
    │
    ▼
POST /token  ──►  TokenController
                      │
                      ▼  dispatches to matching strategy
                  TokenStrategy (interface)
                      │
                      ├── TopUpTokenStrategy          (TOP_UP)
                      ├── ClearCreditTokenStrategy    (CLEAR_CREDIT)
                      ├── ClearTamperTokenStrategy    (CLEAR_TAMPER)
                      └── SetPowerLimitTokenStrategy  (SET_POWER_LIMIT)
                              │
                              ▼
                      STS domain objects + nativetoken generators
                              │
                              ▼
                      BouncyCastle STA / EA07
                              │
                              ▼
                      20-digit IEC 62055-41 token string
```

Adding a new token type is a single-file change — create one `TokenStrategy` implementation and Spring picks it up automatically.

**Key dependencies**

| Library | Purpose |
|---|---|
| Spring Boot 3.4 | HTTP server and dependency injection |
| BouncyCastle 1.70 | STS cryptographic algorithms (STA/EA07) |
| Joda-Time 2.13 | IEC 62055-41 date/time handling |
| springdoc-openapi | Interactive OpenAPI / Swagger UI |

---

## Prerequisites

- Java 17+

No local Maven installation is required — the repository includes the **Maven wrapper** (`mvnw`).

---

## Building

```bash
./mvnw clean package -DskipTests
```

The build produces `target/nxt-sts-*.jar`. The `target/` directory is git-ignored; build artifacts are never committed.

---

## Testing

Run all tests:

```bash
./mvnw test
```

Full build with tests (recommended before committing):

```bash
./mvnw verify
```

Run a single test class:

```bash
./mvnw test -Dtest=TokenStrategyIntegrationTest
```

Run a single test method:

```bash
./mvnw test -Dtest=TokenControllerValidationTest#rejectsMissingRandomNumber
```

Tests live under `src/test/java/co/nxtgrid/` and cover STS token vectors, input validation, and the root service index.

---

## Running

During development:

```bash
./mvnw spring-boot:run
```

To run the packaged JAR (final check before deployment):

```bash
./mvnw clean package
java -jar target/nxt-sts-*.jar
```

The service starts on **port 8080** by default. Override with:

```bash
java -jar target/nxt-sts-*.jar --server.port=8084
```

---

## Docker

The Dockerfile is a multi-stage build — no local Maven or pre-built JAR is required.

```bash
# Build the image (builds the JAR inside the container)
docker build -t nxt-sts .

# Run the container
docker run -p 8080:8080 nxt-sts
```

To pass configuration at runtime:

```bash
docker run -p 8080:8080 \
  -e SERVER_PORT=8080 \
  nxt-sts
```

When using a custom port, set `SERVER_PORT` and map the same host port:

```bash
docker run -p 9090:9090 \
  -e SERVER_PORT=9090 \
  nxt-sts
```

> **Health checks**
>
> The image includes a Docker `HEALTHCHECK` that probes `/actuator/health` every 30 seconds
> on the port given by `SERVER_PORT` (default 8080).
> When running with plain Docker, you can disable it if your environment performs its own
> checks: `docker run --no-healthcheck ...`
>
> **Note:** Some cloud platforms (e.g. DigitalOcean App Platform) use their own health-check
> mechanism instead of the image `HEALTHCHECK`. Configure the platform to probe
> `/actuator/health` (or use its default TCP check on port 8080) — the Dockerfile health
> check is not used in those environments.

---

## CI / Container Image

Every push to `main` and every pull request runs `./mvnw verify` via GitHub Actions (`build.yml`).

Tagged releases (e.g. `v1.2.0`) automatically publish a Docker image to the GitHub Container Registry:

```
ghcr.io/nxtgrid/nxt-sts:v1.2.0
ghcr.io/nxtgrid/nxt-sts:latest
```

Pull and run a released image:

```bash
docker run -p 8080:8080 ghcr.io/nxtgrid/nxt-sts:latest
```

---

## API Documentation

The service is self-describing. After starting it locally:

| Endpoint | Description |
|---|---|
| `GET /` | JSON service index — lists all endpoints and links |
| `GET /swagger` | Interactive Swagger UI (preferred reference for integrators) |
| `GET /v3/api-docs` | Machine-readable OpenAPI JSON |
| `GET /actuator/health` | Health check — returns `{"status":"UP"}` |

---

## API Reference

### `POST /token`

Generates a prepayment token.

**Request body (JSON)**

| Field | Type | Required | Description |
|---|---|---|---|
| `type` | `string` | Yes | Token type: `TOP_UP`, `CLEAR_CREDIT`, `CLEAR_TAMPER`, `SET_POWER_LIMIT` |
| `issueDate` | `string` | Yes | ISO 8601 datetime (see note below) |
| `randomNumber` | `integer` | Yes | STS 4-bit RND field — **must be 0–15** (see note below) |
| `decoderKey` | `string` | Yes | Meter decoder key as a hexadecimal string (16 hex chars = 8 bytes) |
| `kwh` | `number` | For `TOP_UP` | Amount of electricity credit in kWh |
| `powerLimit` | `integer` | For `SET_POWER_LIMIT` | Maximum power limit value |

> **`randomNumber` — STS protocol constraint**
>
> This field maps directly to the 4-bit RND field in the IEC 62055-41 token structure.
> The protocol defines it as a 4-bit value, so **only 0–15 is valid** — this is not an
> arbitrary API limit. Values outside this range will be rejected with HTTP 400.
>
> Vary this value between consecutive token issues for the same meter. Meters reject
> tokens with the same `randomNumber` as the most recently accepted token to prevent
> replay attacks. A value of 0 is valid but should not be reused immediately.
>
> See the full schema in the [Swagger UI](http://localhost:8080/swagger).

> **`issueDate` — wall-clock semantics**
>
> Accepts ISO 8601 forms such as `"2024-03-15T10:30:00"`, `"2026-07-07T10:12:54.289"`,
> or `"2026-07-07T10:12:54.289Z"`. Optional fractional seconds and UTC/offset suffixes
> are allowed. Any time-zone offset is **ignored**; the date and time fields are passed
> to token generation unchanged.

**Example — TOP_UP**

```bash
curl -X POST http://localhost:8080/token \
  -H "Content-Type: application/json" \
  -d '{
    "type": "TOP_UP",
    "issueDate": "2024-03-15T10:30:00",
    "randomNumber": 3,
    "decoderKey": "XXXXXXXXXXXXXXXX",
    "kwh": 0.5
  }'
```

**Response**

```json
{
  "token": "12345678901234567890"
}
```

**Error responses**

| HTTP | Cause |
|---|---|
| `400` | Missing required field, invalid `type`, `randomNumber` out of range (0–15), malformed `issueDate` |
| `500` | Unexpected internal error (check logs) |

> **Security note:** The decoder key is a sensitive credential specific to each meter. It must be transmitted only over encrypted channels (HTTPS) and never logged or stored in plaintext.

---

## Configuration

`src/main/resources/application.properties` is committed with safe defaults. All settings can be overridden via environment variables (Spring Boot relaxed binding) or JVM arguments.

| Property | Env variable | Default | Description |
|---|---|---|---|
| `server.port` | `SERVER_PORT` | `8080` | HTTP listen port |
| `server.error.whitelabel.enabled` | — | `false` | Returns JSON errors instead of HTML pages |
| `management.endpoints.web.exposure.include` | — | `health,info` | Actuator endpoints exposed |
| `management.endpoint.health.show-details` | — | `never` | Hides internals from health response |
| `spring.application.name` | `SPRING_APPLICATION_NAME` | `nxt-sts` | Service name |
| `springdoc.swagger-ui.path` | — | `/swagger` | Swagger UI path |
| `springdoc.api-docs.path` | — | `/v3/api-docs` | OpenAPI JSON path |

For production deployments pass all secrets (decoder keys, credentials) as environment variables or secret volumes — never hard-code them in source.

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

---

## Authors

See [AUTHORS.md](AUTHORS.md).

---

## License

Copyright (C) 2024–2026 Bobby Bol, Tommaso Girotto.

This program is free software: you can redistribute it and/or modify it under the terms of the **GNU Affero General Public License** as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

See [LICENSE](LICENSE) for the full license text.

This project is a derivative work of [NectarAPI/tokens-service](https://github.com/NectarAPI/tokens-service) (AGPL-3.0). See [NOTICE](NOTICE) for attribution details.
