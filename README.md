# NXT STS

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL_v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

NXT STS is a lightweight Spring Boot microservice that generates and manages prepayment tokens compliant with the **IEC 62055-41 (STS)** standard. It is used in production to power prepaid utility metering workflows and exposes a simple REST API for token generation.

> This project is a derivative work of [NectarAPI/tokens-service](https://github.com/NectarAPI/tokens-service), which is licensed under AGPL-3.0. See [NOTICE](NOTICE) for full attribution.

---

## Supported Token Types

Four token types are exposed via `POST /token` today. The underlying STS library implements many more; see **[docs/capabilities.md](docs/capabilities.md)** for the full matrix (generate, decode, and REST API columns).

| API `type` | STS class / subclass | Description | REST API |
|---|---|---|:---:|
| `TOP_UP` | 0 / 0 | Transfer electricity credit (kWh) | **Yes** |
| `CLEAR_CREDIT` | 2 / 1 | Clear existing credit on meter | **Yes** |
| `CLEAR_TAMPER` | 2 / 5 | Clear tamper condition | **Yes** |
| `SET_POWER_LIMIT` | 2 / 0 | Set maximum power limit | **Yes** |

Tokens are generated using the **Standard Transfer Algorithm (STA / EA07)** via the [Bouncy Castle](https://www.bouncycastle.org/) cryptographic library.

---

## Architecture

```
HTTP client
    │
    ▼
POST /token  (Spring Boot REST controller — StsApplication)
    │
    ├── Parses RequestData (type, issueDate, randomNumber, kwh, decoderKey, powerLimit)
    │
    ├── Builds token domain objects (TokenIdentifier, RandomNo, Amount, DecoderKey …)
    │
    └── Delegates to the appropriate token generator:
            TransferElectricityCreditTokenGenerator  (TOP_UP)
            ClearCreditTokenGenerator                (CLEAR_CREDIT)
            ClearTamperConditionTokenGenerator       (CLEAR_TAMPER)
            SetMaximumPowerLimitTokenGenerator       (SET_POWER_LIMIT)
                │
                └── STS crypto via BouncyCastle (STA / EA07)
                        │
                        └── Returns 20-digit IEC 62055-41 token string
```

**Key dependencies**

| Library | Purpose |
|---|---|
| Spring Boot 3.4 | HTTP server and dependency injection |
| BouncyCastle 1.70 | STS cryptographic algorithms (STA/EA07) |
| Joda-Time 2.13 | IEC 62055-41 date/time handling |
| Apache Thrift 0.18 | Optional Prism HSM integration |
| Jedis 5.2 | Optional Redis connectivity |

---

## Prerequisites

- Java 17+
- Maven 3.8+

---

## Building

```bash
mvn clean install -DskipTests
```

The build produces `target/nxt-sts-1.0.0.jar`. The `target/` directory is git-ignored; build artifacts are never committed.

---

## Testing

Run all tests:

```bash
mvn test
```

Full build with tests (recommended before committing):

```bash
mvn verify
```

Run a single test class:

```bash
mvn test -Dtest=TokenStrategyIntegrationTest
```

Run a single test method:

```bash
mvn test -Dtest=TokenControllerValidationTest#rejectsMissingRandomNumber
```

Tests live under `src/test/java/co/nxtgrid/` and cover STS token vectors, input validation, and the root service index.

---

## Running

During development:

```bash
mvn spring-boot:run
```

To run the packaged JAR (final check before deployment):

```bash
mvn clean package
java -jar target/nxt-sts-1.0.0.jar
```

The service starts on **port 8080** by default (Spring Boot embedded Tomcat). Override with:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8084
```

Or, when using the JAR:

```bash
java -jar target/nxt-sts-1.0.0.jar --server.port=8084
```

---

## Docker

```bash
# 1. Build the JAR first
mvn clean install -DskipTests

# 2. Build the image
docker build -t nxt-sts .

# 3. Run the container
docker run -p 8080:8080 nxt-sts
```

---

## API Reference

### `POST /token`

Generates a prepayment token.

**Request body (JSON)**

| Field | Type | Required | Description |
|---|---|---|---|
| `type` | `string` | Yes | Token type: `TOP_UP`, `CLEAR_CREDIT`, `CLEAR_TAMPER`, `SET_POWER_LIMIT` |
| `issueDate` | `string` | Yes | ISO 8601 datetime, e.g. `"2024-03-15T10:30:00"` |
| `randomNumber` | `integer` | Yes | Random 4-bit value (0–15) used for token uniqueness |
| `decoderKey` | `string` | Yes | Meter decoder key as a hexadecimal string (16 hex chars = 8 bytes) |
| `kwh` | `number` | For `TOP_UP` | Amount of electricity credit in kWh |
| `powerLimit` | `integer` | For `SET_POWER_LIMIT` | Maximum power limit value |

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

> **Security note:** The decoder key is a sensitive credential specific to each meter. It must be transmitted only over encrypted channels (HTTPS) and never logged or stored in plaintext.

---

## Configuration

NXT STS uses Spring Boot defaults. Configuration can be supplied via:

- `src/main/resources/application.properties` or `application.yml` (not committed; add to suit your deployment)
- Environment variables (standard Spring Boot relaxed binding)
- JVM arguments, e.g. `--server.port=8084`

For production deployments on Digital Ocean or Kubernetes, pass all secrets (keys, credentials) as environment variables or secret volumes — never hard-code them in source.

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
