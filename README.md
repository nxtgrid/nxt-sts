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
| `TOP_UP_KWH` | 0 / 0 | Transfer electricity credit (kWh) | **Yes** |
| `CLEAR_CREDIT` | 2 / 1 | Clear existing credit on meter | **Yes** |
| `CLEAR_TAMPER` | 2 / 5 | Clear tamper condition | **Yes** |
| `SET_POWER_LIMIT` | 2 / 0 | Set maximum power limit | **Yes** |

`TOP_UP` is still accepted as a **deprecated** wire alias for `TOP_UP_KWH` (same path) for older
callers. Prefer `TOP_UP_KWH` for new integrations.

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
                      ├── TransferElectricityCreditStrategy  (TOP_UP_KWH)
                      ├── ClearCreditStrategy                (CLEAR_CREDIT)
                      ├── ClearTamperStrategy                (CLEAR_TAMPER)
                      └── SetMaximumPowerLimitStrategy       (SET_POWER_LIMIT)
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

### Package boundary (future `sts-core`)

The repository is a single Maven module today, but the code is already split so the STS
engine can be extracted later without a redesign:

| Layer | Packages | Allowed dependencies |
|---|---|---|
| **Core** (future `sts-core`) | `co.nxtgrid.token.*` | Java, BouncyCastle, Joda-Time only — **no Spring** |
| **Wrapper** (this service) | `co.nxtgrid.api.*`, `co.nxtgrid.strategy.*`, `StsApplication` | Spring Boot, validation, OpenAPI, etc. |

- Domain objects and `nativetoken` generators live under `token/` and have no Spring imports.
- `TokenStrategy` classes live in the wrapper: they translate HTTP DTOs into domain calls.
  They are **not** part of the future library artifact.
- Embedding STS generation in another JVM process means depending on core (`token/` packages /
  generators), not on `TokenStrategy` or the REST DTOs.

See [ADR-001 decision 7](docs/architecture/001-open-source-preparation.md) for the full
constraint. Publishing a standalone `sts-core` artifact is deferred until there is a concrete
in-process consumer — see [Roadmap](#roadmap).

**Key dependencies**

| Library | Purpose |
|---|---|
| Spring Boot 3.4 | HTTP server and dependency injection |
| BouncyCastle `bcprov-jdk15on:1.70` | STS cryptographic algorithms (STA/EA07). Legacy Maven coordinate — planned upgrade to `bcprov-jdk18on` (see [Roadmap](#roadmap)) |
| Joda-Time 2.13 | IEC 62055-41 date/time handling |
| springdoc-openapi | Interactive OpenAPI / Swagger UI |

---

## Roadmap

Phases 1–3 of open-source preparation are complete (dead-code removal, HTTP hardening, Docker /
CI / docs). The following are **intentionally deferred** until someone needs them:

| Next step | What it unlocks | When to start |
|---|---|---|
| **Extract `sts-core`** | Publish the Spring-free STA engine as a Maven artifact for embedding in other JVM services | A concrete in-process consumer exists |
| **Conformance vectors (`test-vectors.json`)** | Language-neutral golden tokens shared by ports and regression suites | Part of / prerequisite for extraction and ports |
| **Multi-language ports** | TypeScript, PHP, Python (etc.) implementations validated against the same vectors | After conformance vectors exist |
| **`POST /decode` / HSM** | Token decode API or hardware key storage | Separate product request; not required for library extraction |
| **Upgrade BouncyCastle** | Move from `bcprov-jdk15on:1.70` to `bcprov-jdk18on` (supported artifact line) | Maintenance / security hygiene; re-run `./mvnw verify` and confirm token vectors unchanged |

Until `sts-core` is published, use this service over HTTP, or call the `co.nxtgrid.token.*`
generators in-process from a checkout of this repository. Details: engineering plan
[Phase 4 / 5](docs/plans/001-open-source-preparation.md).

---

## Prerequisites

- Java 17+ (for local Maven builds; not required if you only pull the container image)
- Docker (optional, for the container path below)

No local Maven installation is required — the repository includes the **Maven wrapper** (`mvnw`).

---

## Quick start

Deploy the service, confirm it is up, then generate a token.

**1. Run** (pick one):

```bash
# Released image from GHCR
docker run --rm -p 8080:8080 ghcr.io/nxtgrid/nxt-sts:latest

# Or build and run from this repo
docker build -t nxt-sts . && docker run --rm -p 8080:8080 nxt-sts

# Or run from source (Java 17+)
./mvnw spring-boot:run
```

**2. Health check:**

```bash
curl -s http://localhost:8080/actuator/health
# {"status":"UP"}
```

**3. Generate a token** (replace the decoder key with a real 16-hex-char meter key):

```bash
curl -X POST http://localhost:8080/token \
  -H "Content-Type: application/json" \
  -d '{
    "type": "TOP_UP_KWH",
    "issueDate": "2024-03-15T10:30:00",
    "randomNumber": 3,
    "decoderKey": "XXXXXXXXXXXXXXXX",
    "kwh": 0.5
  }'
```

Interactive API explorer: [http://localhost:8080/swagger](http://localhost:8080/swagger).  
More detail: [Docker](#docker), [Running](#running), [API Reference](#api-reference).

**Production:** run the same container image on your cloud or host (App Platform, ECS, Cloud Run, a VM, Kubernetes, etc.). Map port **8080**, point the platform health check at **`/actuator/health`**, then call **`POST /token`** from your backend over HTTP. No special STS-specific deploy steps beyond a normal container service.

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

Tagged releases (e.g. `v1.2.0`) automatically publish a multi-arch Docker image
(`linux/amd64` and `linux/arm64`) to the GitHub Container Registry:

```
ghcr.io/nxtgrid/nxt-sts:v1.2.0
ghcr.io/nxtgrid/nxt-sts:latest
```

Pull and run a released image (Apple Silicon and x86 hosts pick the matching arch):

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
| `type` | `string` | Yes | Token type: `TOP_UP_KWH`, `CLEAR_CREDIT`, `CLEAR_TAMPER`, `SET_POWER_LIMIT` (deprecated alias: `TOP_UP` → `TOP_UP_KWH`) |
| `issueDate` | `string` | Yes | ISO 8601 datetime (see note below) |
| `randomNumber` | `integer` | Yes | STS 4-bit RND field — **must be 0–15** (see note below) |
| `decoderKey` | `string` | Yes | Meter decoder key as a hexadecimal string (16 hex chars = 8 bytes) |
| `kwh` | `number` | For `TOP_UP_KWH` | Amount of electricity credit in kWh (also required when using deprecated `TOP_UP`; see quantization note below) |
| `powerLimit` | `integer` | For `SET_POWER_LIMIT` | Maximum power limit (see note below) |

> **`randomNumber` — STS protocol constraint (and same-minute uniqueness)**
>
> This field maps directly to the 4-bit RND field in the IEC 62055-41 token structure.
> The protocol defines it as a 4-bit value, so **only 0–15 is valid** — this is not an
> arbitrary API limit. Values outside this range will be rejected with HTTP 400.
>
> The STS token identifier (TID) is **minute-granular**: only the UTC date and minute of
> `issueDate` enter the TID (seconds and sub-seconds are ignored). So `10:30:00` and
> `10:30:59` produce the same TID. With identical `decoderKey`, amount, and other
> fields, two requests in the same wall-clock minute produce a **byte-identical token**
> unless `randomNumber` differs.
>
> Callers should therefore **track the last-used RND per meter** and advance it for each
> new issue (especially when vending more than once in the same minute). Meters also
> reject a token that reuses the same `randomNumber` as the most recently accepted token
> (anti-replay). A value of 0 is valid but should not be reused immediately. With only
> 16 possible RND values, high-frequency same-minute vending on one meter will exhaust
> the space unless the caller waits for the next minute or otherwise avoids collisions.
>
> See the full schema in the [Swagger UI](http://localhost:8080/swagger).

> **`issueDate` — wall-clock semantics (UTC)**
>
> Accepts ISO 8601 forms such as `"2024-03-15T10:30:00"`, `"2026-07-07T10:12:54.289"`,
> or `"2026-07-07T10:12:54.289Z"`. Optional fractional seconds and UTC/offset suffixes
> are allowed. Any time-zone offset is **ignored**; the date and time fields are
> interpreted as **UTC** for TID calculation, independent of the server's timezone.
>
> TID uses **minute resolution** only — changing seconds within the same minute does not
> change the token. For uniqueness of consecutive issues, vary `randomNumber` (see above).

> **`kwh` — amount quantization (0.1 kWh steps)**
>
> The STS transfer-amount field does not store an arbitrary floating-point kWh value.
> Credit is encoded in **tenths of a kWh** (0.1 kWh steps). Before packing into the
> token, this service maps the request `kwh` onto that grid as follows (inherited from
> [NectarAPI/tokens-service](https://github.com/NectarAPI/tokens-service); unchanged in NXT STS):
>
> | Requested `kwh` | Mapping | Effective credit on the token |
> |---|---|---|
> | `< 1` | ceil to the next 0.1 kWh | e.g. `0.01` → **0.1**, `0.11` → **0.2**, `0.5` → **0.5** |
> | `≥ 1` | truncate toward zero to a 0.1 kWh step | e.g. `1.19` → **1.1**, `1.99` → **1.9** |
>
> Very small top-ups therefore cannot encode as zero (`0.01` becomes `0.1`). Larger
> amounts drop any leftover fraction of a tenth rather than rounding up.
>
> **Recommendation for callers / MPM:** send `kwh` values that are already multiples of
> `0.1` so the mapping is exact, and treat billing/ledger amounts as that quantized
> value (not an unrounded intermediate float). Changing this rule would alter token
> output for the same inputs and break compatibility with existing meters and systems.
>
> **Maximum:** `kwh` must not exceed **1820162.4** (the STS 16-bit amount field maximum).
> Larger values are rejected with HTTP 400.

> **`powerLimit` — STS maximum**
>
> `SET_POWER_LIMIT` uses the same 16-bit STS amount encoding as credit tokens, but the
> request value is **not** scaled by 10 (unlike `kwh`). `powerLimit` must be an integer
> from **0** to **18201624**. Larger values are rejected with HTTP 400.

**Example — TOP_UP_KWH**

```bash
curl -X POST http://localhost:8080/token \
  -H "Content-Type: application/json" \
  -d '{
    "type": "TOP_UP_KWH",
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
