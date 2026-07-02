# ADR-001: NXT STS Open-Source Preparation

**Date:** 2026-07-02
**Status:** Accepted — execution tracked in `docs/plans/001-open-source-preparation.md`

---

## Context

`nxt-sts` is a Spring Boot microservice that generates IEC 62055-41 (STS) prepayment tokens
using the Standard Transfer Algorithm (STA / EA07). It is a derivative work of
[NectarAPI/tokens-service](https://github.com/NectarAPI/tokens-service) (AGPL-3.0), with a
thin Spring Boot HTTP wrapper added over the original cryptographic engine.

The repository is already public and AGPL-3.0 licensed, with LICENSE, NOTICE, CONTRIBUTING,
and AUTHORS files in place. However, the codebase as it stands is not ready to be used or
contributed to by external parties, for three reasons:

1. **Massive dead code burden.** The repository contains 351 Java files. The live HTTP path
   (`MyApplication` → `POST /token`) touches roughly 60–80 of them. The remainder — the full
   NectarAPI service layer (`co.nxtgrid.tokens.service.*`), the Prism HSM integration
   (`co.nxtgrid.hsm.prism.*`), both the `prism` and `nativetoken` token decoder paths, and a
   Redis/Thrift-backed request utility — are unreachable from the running application. Five
   dependencies (Jedis, Thrift, Gson, `org.json`, `jakarta.xml.bind`) exist solely for this
   dead code.

2. **Fragile, opaque HTTP surface.** The single controller (`MyApplication`) conflates
   application bootstrap with request handling, accepts all HTTP verbs on `/token`, returns
   `null` (HTTP 200 with empty body) on both unknown token types and unhandled exceptions,
   hardcodes an internal `requestID = "asda"`, and contains no input validation despite having
   `spring-boot-starter-validation` on the classpath. Adding a new token type requires editing
   an `if/else` chain and knowing the exact constructor signature of the target generator class.

3. **No deployment scaffolding.** The Dockerfile is single-stage, copies a pre-built JAR (not
   built inside the container), has no image tag pin, and runs as root. There is no CI pipeline,
   no Maven wrapper, no committed `application.properties`, no `src/test` directory, and no
   health endpoint.

The service has production value: STS token generation is a domain-specific capability that
any mini-grid or prepaid utility operator needs, and the BouncyCastle-based STA implementation
is correct. The goal of this ADR is to make the project genuinely usable and contributable as
open-source software without changing its behavior.

---

## Decisions

### 1. Delete all dead code in one focused pass

The entire `co.nxtgrid.tokens.service.*` package (the original NectarAPI service layer:
`TokensService`, `TokensServiceImpl`, validation rules, decoder manager, request utilities,
timeline requests, and associated DTOs and exceptions) will be deleted.

The Prism HSM integration (`co.nxtgrid.hsm.prism.*`) and the parallel `prism`-flavour
generator tree (`...tokensgenerator.prism.*`) will be deleted. Neither is instantiated nor
reachable from the running application. If HSM support is added in the future it will be
designed from scratch against the real HSM contract.

The `tokensdecoder.*` packages will be deleted unless a decode endpoint is explicitly added
(see decision 2). Decoder classes are unused by the live path.

The `co.nxtgrid.ca.*` symmetric cipher wrappers will be evaluated per-file: delete those
that are only referenced from the deleted packages; retain any that the live generator path
transitively uses.

After deletion, the five orphaned Maven dependencies (Jedis, Thrift, Gson, `org.json`,
`jakarta.xml.bind`) are removed from `pom.xml`.

### 2. Keep the service as a REST microservice; do not introduce a DB or state

The service remains stateless. Each `POST /token` call is a pure function: given the key
and parameters, return a token. No persistence layer is introduced. This is the correct
design for a cryptographic primitive service.

A `POST /decode` endpoint may be added later (the decoder domain objects already exist),
but is explicitly out of scope for this preparation effort.

### 3. Harden the HTTP controller with a strategy pattern

`MyApplication` is split into three concerns:

- `StsApplication` — the `@SpringBootApplication` bootstrap class only.
- `TokenController` — the `@RestController` handling `POST /token`, with `@PostMapping`,
  proper `@Valid` input validation, and `@RestControllerAdvice` error handling that maps
  exceptions to structured JSON error responses with correct HTTP status codes.
- `TokenStrategy` — an interface (`supports(type: String): Boolean` + `generate(request):
  Token`) implemented by one class per token type. Spring collects all implementations and
  the controller dispatches to the matching strategy. Adding a new token type requires adding
  one class only; no existing file changes.

The hardcoded `requestID = "asda"` is replaced by a generated UUID per request. The
hardcoded `KeyExpiryNumber(255)` and `BaseDate._2014` are retained as sensible STS defaults
but moved to named constants in the appropriate strategy class, with a comment explaining
the STS standard meaning.

### 4. Replace string-based type dispatch with an enum

Token types (`TOP_UP`, `CLEAR_CREDIT`, `CLEAR_TAMPER`, `SET_POWER_LIMIT`) are defined as a
Java enum `TokenType` rather than compared as raw strings. The enum carries its STS class
and subclass as metadata, making the system self-documenting for contributors unfamiliar
with IEC 62055-41. The controller's `@RequestBody` DTO deserializes the `type` field into
`TokenType` directly, with a clear 400 response for unrecognized values.

### 5. Multi-stage Dockerfile; Maven wrapper; committed default config

The Dockerfile is rewritten as a proper multi-stage build: a Maven build stage produces the
JAR; a slim `eclipse-temurin:17-jre` runtime stage copies only the artifact and runs as a
non-root user. The image tag is pinned to a specific digest.

The Maven wrapper (`mvnw` / `.mvn/`) is added so contributors do not need a local Maven
installation.

A minimal `src/main/resources/application.properties` is committed with safe defaults
(`server.port=8080`, `logging.level.root=INFO`). A `Spring Boot Actuator` dependency is added
to expose `/actuator/health` for container orchestration readiness checks.

### 6. CI via GitHub Actions; no CD in scope

A `.github/workflows/build.yml` pipeline is added that runs `mvn verify` on every PR and
push to `main`. No deployment pipeline (CD) is defined at this stage; operators are expected
to build and deploy the container image themselves using the provided Dockerfile and README.
A GitHub release workflow that publishes the image to GHCR is added alongside the CI pipeline
so tagged releases produce a usable artifact automatically.

### 7. Optional future: core library extraction and multi-language ports

Extracting the STA/EA07 cryptographic engine into a standalone `sts-core` artifact (no Spring,
published to Maven Central) is the right next step after this preparation, enabling the Java
implementation to be embedded in other JVM services. Multi-language ports (TypeScript/npm, PHP/
Composer, Python/PyPI) are feasible because the STA algorithm is deterministic and can be
validated against shared test vectors; however, they represent a separate, significant effort
and are explicitly deferred. The shared conformance test vectors that would anchor any port
are a deliverable of this preparation effort (see decision 3 / task: add tests with known
STS vectors).

---

## Consequences

### Positive

- The active codebase shrinks from 351 files to roughly 60–80, making the project
  approachable for external contributors.
- The `TokenStrategy` pattern makes adding a new token type a single-file change, documented
  clearly in `CONTRIBUTING.md`.
- Proper error handling and input validation make the API safe to expose to external callers.
- A CI pipeline and multi-stage Docker build are the baseline that open-source projects require
  to receive contributions confidently.
- Known-good STS test vectors established in this effort are the foundation for any future
  language port.

### Negative / Risks

- Deleting the dead code removes implementation examples that might have informed a future
  Prism HSM or decode feature. The NectarAPI upstream source is still available via the NOTICE
  attribution if needed.
- The `TokenStrategy` interface is designed against the four existing token types. If a future
  token type requires constructor parameters with no analogue in any current type (e.g., a
  multi-block key-change token), the interface may need extension. Per ADR-001 decision 2 in
  the NectarAPI pattern (wait for a second real case before generalizing), resist generalizing
  until a concrete second case exists.
- BouncyCastle `bcprov-jdk15on:1.70` (the legacy artifact) is the current dependency.
  Migration to `bcprov-jdk18on` is a security maintenance concern; verify the API surface is
  compatible before upgrading.

## Triggers (revisit when)

- A fifth token type is implemented (validates the `TokenStrategy` SPI).
- A second language port is being planned (triggers conformance test vector formalization).
- An operator reports the need for HSM-backed key storage (triggers Prism or PKCS#11 design).

## Related

- **NOTICE** — NectarAPI upstream attribution; governs what can be deleted vs. what is
  derived.
- **CONTRIBUTING.md** — must be updated after the `TokenStrategy` pattern is in place to
  describe the "how to add a token type" workflow.
- **Execution plan** — `docs/plans/001-open-source-preparation.md`
