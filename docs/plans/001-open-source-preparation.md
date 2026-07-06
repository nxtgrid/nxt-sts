# NXT STS Open-Source Preparation — Engineering Plan

**Decision:** ADR-001 (`docs/architecture/001-open-source-preparation.md`)
**Plan number:** 001
**Created:** 2026-07-02
**Status:** In progress (Phase 1 — Tasks 1.1–1.2 complete)

---

## Reading guide for AI agents

This document is the **single source of truth for picking up and executing** the NXT STS
open-source preparation. Every task is written to be self-contained: it tells you what the
current code looks like, what the target looks like, which files to touch, and what "done"
means. You do not need the full conversation history that produced this plan.

**Before starting any task:**
1. Read the relevant "Current state" section of the task.
2. Read the actual source files referenced — they are the ground truth; this document
   describes the state at the time it was written (2026-07-02) and may drift.
3. Execute one task at a time. After completing a task, mark it `[x]` in this file and
   note any decisions or deviations under the task.
4. Respect the **Depends on** annotations — do not start a task before its dependencies
   are complete.

**Phases must be executed in order.** Tasks within a phase may be parallelised unless
a `Depends on` note says otherwise.

---

## Current codebase snapshot

> This section gives a cold agent enough context to orient before reading individual tasks.

### Repository layout (non-test, non-target)

```
nxt-sts/
├── pom.xml                        ← Maven build; artifact id: jambu (to be renamed)
├── Dockerfile                     ← Single-stage; copies pre-built JAR; no tag pin
├── README.md                      ← Reasonably complete; gaps noted in tasks
├── LICENSE / NOTICE / AUTHORS.md / CONTRIBUTORS.md / CONTRIBUTING.md
└── src/main/java/co/nxtgrid/
    ├── MyApplication.java         ← @SpringBootApplication + @RestController combined
    ├── RequestData.java           ← Request DTO (no validation annotations)
    ├── DateTimeConverter.java     ← Joda-Time ISO 8601 converter (Spring @Component)
    ├── token/                     ← STS domain model (derived from NectarAPI) — KEEP
    │   ├── domain/                ← Value objects: Amount, DecoderKey, TokenIdentifier, etc.
    │   ├── exceptions/            ← ~50 domain exception classes
    │   └── generators/
    │       ├── decoderkeygenerator/
    │       ├── tokensdecoder/     ← Token decode path — NOT wired up; delete
    │       └── tokensgenerator/
    │           ├── nativetoken/   ← LIVE: the four active generators
    │           └── prism/         ← Dead: HSM-backed generator path — delete
    ├── ca/                        ← BouncyCastle cipher wrappers — partially dead; audit
    ├── hsm/prism/                 ← Dead: Prism HSM client (Thrift) — delete entire package
    └── tokens/                    ← Dead: NectarAPI service layer — delete entire package
        ├── service/               ← TokensService interface + TokensServiceImpl (not @Service)
        ├── utils/request/         ← RequestUtils (commented-out @Component; HTTP to NectarAPI)
        ├── response/              ← ApiResponse DTO
        ├── entity/                ← Token entity
        └── constant/              ← StringConstants
```

### The live request path (everything outside this is dead)

```
POST /token
    └── MyApplication.home(RequestData body)
            ├── Constructs TokenIdentifier, RandomNo, Amount, DecoderKey, KeyExpiryNumber
            ├── Dispatches on body.getType() with if/else:
            │       "TOP_UP"          → TransferElectricityCreditTokenGenerator
            │       "CLEAR_CREDIT"    → ClearCreditTokenGenerator
            │       "CLEAR_TAMPER"    → ClearTamperConditionTokenGenerator
            │       "SET_POWER_LIMIT" → SetMaximumPowerLimitTokenGenerator
            └── Returns Map<String, Object> { "token": <20-digit string> }
                (returns null → HTTP 200 empty body on unknown type or exception)
```

All four generators live under:
`src/main/java/co/nxtgrid/token/generators/tokensgenerator/nativetoken/`

### Active Maven dependencies

| Dependency | Used by live path? | Action |
|---|---|---|
| `spring-boot-starter-web` | Yes | Keep |
| `spring-boot-starter-validation` | Yes (unused — add `@Valid`) | Keep |
| `bcprov-jdk15on:1.70` | Yes (STA crypto) | Keep; plan upgrade to `bcprov-jdk18on` |
| `joda-time:2.13` | Yes (`DateTimeConverter`, generators) | Keep for now |
| `gson:2.11` | Dead (`tokens/utils/`) | Delete with package |
| `libthrift:0.18.1` | Dead (`hsm/prism/`) | Delete with package |
| `jedis:5.2` | Dead (`tokens/utils/`) | Delete with package |
| `json:20240303` | Dead (`tokens/utils/`) | Delete with package |
| `jakarta.xml.bind-api:4.0.2` | Dead (`tokens/utils/`) | Delete with package |
| `junit-jupiter-api:5.11` | Test | Keep; add test classes |

---

## Goals

- The live codebase reflects only what is actually running: domain model + four generators +
  a clean HTTP wrapper.
- Adding a new token type requires creating one class that implements `TokenStrategy` only;
  no existing file changes.
- The service returns correct HTTP status codes for all error cases and never returns a null
  or empty body.
- `GET /` returns a JSON service index; unknown routes return JSON errors (no whitelabel HTML).
- OpenAPI/Swagger UI documents the API, including the `randomNumber` field (STS RND, 0–15).
- A CI pipeline builds and tests on every PR. A GitHub release workflow publishes a container
  image to GHCR on tag.
- `docker-compose up` starts the service locally with no prior knowledge of Spring Boot.
- Known-good STS token vectors are encoded as JUnit tests, providing a regression safety net
  for future changes.

## Non-goals (this preparation effort)

- A token decode endpoint (the decoder domain exists but is not wired up; deferred).
- HSM-backed key storage (Prism or PKCS#11); deferred.
- Persistence / token audit log; the service remains stateless.
- Multi-language ports (TypeScript, PHP, Python); deferred — see ADR-001 decision 7.
- Extraction of `sts-core` as a standalone Maven library; deferred to a follow-up effort.

---

## Architectural invariant — `sts-core` boundary

> **This constraint applies to every task in phases 1–3. It is not deferred. Violating it
> would require a code redesign before Phase 4 can begin.**

The repository's roadmap includes extracting the STS cryptographic engine into a standalone
`sts-core` Maven artifact with no Spring dependency (Phase 4). For that extraction to remain a
mechanical packaging exercise, the boundary between the core and the HTTP wrapper must be clean
from the start of this effort.

### The boundary

| Layer | Packages | Allowed dependencies |
|---|---|---|
| **Core** (future `sts-core`) | `co.nxtgrid.token.*`, `co.nxtgrid.ca.*` | Plain Java, BouncyCastle, Joda-Time — nothing else |
| **Wrapper** (future `sts-service`) | `co.nxtgrid` top-level (controllers, DTOs, strategies, OpenAPI config) | Spring Boot freely; depends on core packages |

### Concrete rules every task must follow

1. **Never add a Spring import to any file under `co.nxtgrid.token.*` or `co.nxtgrid.ca.*`.**
   These packages must compile without Spring on the classpath. They are the future `sts-core`.

2. **`TokenStrategy` implementations live in `co.nxtgrid.strategy.*` (wrapper layer), not in
   `token/`.** They translate an HTTP request into a domain call; that translation belongs in
   the wrapper. They may carry `@Component` as a Spring marker, but removing that annotation
   must leave them as valid, compilable pure-Java classes.

3. **Domain objects (`token/domain/*`) are plain Java value objects.** No `@JsonProperty`,
   no `@Entity`, no `@Component`, no framework annotations of any kind.

4. **The four generator classes under `nativetoken/` are not modified beyond what dead-code
   deletion requires.** They receive domain objects and return domain objects — that is the
   entire contract.

5. **`DateTimeConverter.java` is the only current Spring file in a near-core location; it is
   deleted in Task 2.3.** Until then, do not create any other Spring-annotated file inside
   `co.nxtgrid.token.*` or `co.nxtgrid.ca.*`.

### Verification (run after any task that touches these packages)

```bash
grep -r "import org.springframework" src/main/java/co/nxtgrid/token/
grep -r "import org.springframework" src/main/java/co/nxtgrid/ca/
```

Both commands must return no results when the task is marked done.

---

## Phase 1 — Delete dead code and prune dependencies

**Goal:** reduce the repository to only what the live path uses. No behavior changes.

**Estimated effort:** ~3–4 days

---

### Task 1.1 — Delete `co.nxtgrid.tokens` package
- [x] **Status:** Complete (2026-07-06)

**Deviation:** Two exception classes referenced from the live `token/` path and from
packages deleted in later tasks were relocated to `co.nxtgrid.token.exceptions` before
deletion: `InvalidIndividualAccountIdentificationNumber`, `InvalidTokenNoException`.
Unused import of `DecoderKeyGeneratorManager` removed from `nativetoken/TokenGenerator.java`.
- **Depends on:** nothing

**Current state:**
`src/main/java/co/nxtgrid/tokens/` contains the original NectarAPI service layer. None of it
is wired into Spring (`TokensServiceImpl` has no `@Service`; `RequestUtils` has its `@Component`
commented out). Pulling the thread: nothing in `MyApplication` imports from this package.

Files under this path include:
- `service/TokensService.java` (interface with 10+ unimplemented methods)
- `service/impl/TokensServiceImpl.java`
- `service/impl/generate/` (class0/1/2 generator managers, all unused)
- `service/impl/decoder/TokenDecoderManager.java` (references `hsm.prism`)
- `service/impl/validate/` (RulesNative, RulesPrism, Validator)
- `utils/request/RequestUtils.java`, `ApiResponseException.java`, `CrudOperations.java`,
  `Payload.java`, `BasicAuthCredentials.java`
- `response/ApiResponse.java`
- `entity/Token.java`
- `constant/StringConstants.java`

**Target state:** the entire `src/main/java/co/nxtgrid/tokens/` directory is deleted.

**Done when:** `find src -path "*/co/nxtgrid/tokens/*"` returns no results. Build succeeds.

---

### Task 1.2 — Delete `co.nxtgrid.hsm` package
- [x] **Status:** Complete (2026-07-06)

**Deviation:** The `tokensgenerator/prism/` tree was deleted in the same pass — every prism
generator imports `hsm.prism` and the project cannot compile with one removed and the other
remaining. `Meter.decodePrism()` and its HSM connection fields were removed from
`tokensdecoder/Meter.java` (native decode path retained). Task 1.3 prism portion is therefore
already done; 1.3 now only covers `tokensdecoder/`.
- **Depends on:** 1.1 (decoder manager in `tokens/` references `hsm/`)

**Current state:**
`src/main/java/co/nxtgrid/hsm/prism/` contains the Prism HSM Thrift client
(`PrismHSMConnector`, `PrismClientFacade`, `TokenApi`) and about 15 DTOs (`Token`,
`MeterConfigIn`, `Alert`, `SignInResult`, etc.). These are only referenced by the now-deleted
`tokens/service/impl/decoder/TokenDecoderManager.java`.

**Target state:** the entire `src/main/java/co/nxtgrid/hsm/` directory is deleted.

**Done when:** `find src -path "*/co/nxtgrid/hsm/*"` returns no results. Build succeeds.

---

### Task 1.3 — Delete the `prism` generator tree and `tokensdecoder` packages
- [ ] **Status:** Not started (prism tree already removed in Task 1.2 — `tokensdecoder/` remains)
- **Depends on:** 1.1, 1.2

**Current state:**
`src/main/java/co/nxtgrid/token/generators/tokensgenerator/prism/` mirrors the `nativetoken`
tree but routes through the Prism HSM. It is never called from `MyApplication`.
`src/main/java/co/nxtgrid/token/generators/tokensdecoder/` contains decode-side classes
(decoders for class0/1/2 tokens, `Meter`, `TokenDecoder`, error/state/result subpackages).
Neither tree is reachable from the live path.

**Target state:**
Delete:
- `src/main/java/co/nxtgrid/token/generators/tokensgenerator/prism/` (entire directory)
- `src/main/java/co/nxtgrid/token/generators/tokensdecoder/` (entire directory)

**Done when:** `find src -path "*/tokensgenerator/prism/*" -o -path "*/tokensdecoder/*"` returns
no results. Build succeeds.

---

### Task 1.4 — Audit and prune `co.nxtgrid.ca` package
- [ ] **Status:** Not started
- **Depends on:** 1.1, 1.2, 1.3

**Current state:**
`src/main/java/co/nxtgrid/ca/` contains:
- `Provider.java`, `Metadata.java` — BouncyCastle provider registration helpers
- `keys/GeneralCipher.java`, `keys/Encode.java`
- `keys/symmetric/SymmetricCipher.java`, `AES128Cipher.java`, `DesedeCipher.java`, `DESCipher.java`
- `keys/utils/FixedRandom.java`, `HexByteUtils.java`

Run `grep -r "co.nxtgrid.ca" src/` after completing 1.1–1.3 to determine which of these are
transitively used by the remaining `nativetoken` generator tree. Delete those that are not.

**Done when:** every remaining file in `co.nxtgrid.ca` is imported by at least one live file.
Build succeeds.

---

### Task 1.5 — Remove orphaned Maven dependencies
- [ ] **Status:** Not started
- **Depends on:** 1.1, 1.2, 1.3, 1.4

**Current state:**
`pom.xml` declares Gson, Thrift, Jedis, `org.json`, and `jakarta.xml.bind-api` which exist
solely for the deleted packages.

**Target state:**
Remove from `pom.xml`:
```xml
<dependency><!-- com.google.code.gson:gson --></dependency>
<dependency><!-- org.apache.thrift:libthrift --></dependency>
<dependency><!-- redis.clients:jedis --></dependency>
<dependency><!-- org.json:json --></dependency>
<dependency><!-- jakarta.xml.bind:jakarta.xml.bind-api --></dependency>
```

**Done when:** `mvn clean install -DskipTests` succeeds. `mvn dependency:analyze` reports
no unused declared dependencies (run and inspect — the tool is noisy for Spring Boot but
obvious orphans will be flagged).

---

### Task 1.6 — Rename artifact and main class
- [ ] **Status:** Not started
- **Depends on:** nothing (can run in parallel with 1.1–1.5)

**Current state:**
- `pom.xml` `<artifactId>jambu</artifactId>` — internal codename, confusing in an OSS context.
- `pom.xml` `<version>1.0-SNAPSHOT</version>` — should be semver.
- `MyApplication` — does not communicate purpose.
- Build output: `target/jambu-1.0-SNAPSHOT.jar`
- Dockerfile references: `target/jambu-1.0-SNAPSHOT.jar`

**Target state:**
- `<artifactId>nxt-sts</artifactId>`
- `<version>1.0.0</version>`
- Rename `MyApplication.java` → `StsApplication.java`; update the class name and
  `<mainClass>` in `pom.xml` to `co.nxtgrid.StsApplication`.
- Update `Dockerfile` JAR reference.
- Update README build/run instructions to reference the new JAR name.

**Done when:** `mvn clean install -DskipTests` produces `target/nxt-sts-1.0.0.jar`.
`java -jar target/nxt-sts-1.0.0.jar` starts the service.

---

### Task 1.7 — Fix `.gitignore`; remove tracked `.DS_Store`
- [ ] **Status:** Not started
- **Depends on:** nothing

**Current state:**
`.DS_Store` is tracked by git (visible in `git status`). `.gitignore` does not include it.
`target/*` is correctly ignored, but the exception `!target/jambu-1.0-SNAPSHOT.jar` tracks
a build artifact (the JAR) — this is wrong; build artifacts must not be committed.

**Target state:**
```gitignore
target/
.vscode/
.DS_Store
.idea/
*.iml
```
Remove the `!target/...` exception entirely (the multi-stage Dockerfile will build the JAR
inside the container; it does not need to be committed).
Run `git rm --cached .DS_Store` to untrack the file.

**Done when:** `git status` shows no `.DS_Store`. `git check-ignore -v .DS_Store` confirms
the rule applies.

---

### Phase 1 checkpoint

- `mvn clean install -DskipTests` from a clean clone succeeds.
- `java -jar target/nxt-sts-1.0.0.jar` starts and `POST /token` with a valid `TOP_UP`
  payload returns a 20-digit token.
- `wc -l $(find src -name "*.java")` line count is materially lower than the original 351 files.

---

## Phase 2 — Harden the HTTP wrapper

**Goal:** the `POST /token` endpoint returns correct HTTP status codes and structured error
bodies for all failure cases. Adding a new token type requires one new class only. The API is
self-describing via OpenAPI/Swagger UI and a JSON root route.

**Estimated effort:** ~5–6 days

---

### Task 2.1 — Extract `StsApplication` bootstrap and create `TokenController`
- [ ] **Status:** Not started
- **Depends on:** 1.6 (rename must be complete first)

**Current state:**
`MyApplication` (after rename: `StsApplication`) combines `@SpringBootApplication` and
`@RestController`. The `home()` method handles the full request dispatch inline.

**Target state:**
Split into two files:

`StsApplication.java`:
```java
@SpringBootApplication
public class StsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StsApplication.class, args);
    }
}
```

`TokenController.java`:
```java
@RestController
@RequestMapping("/token")
public class TokenController {
    // Injects List<TokenStrategy> — see Task 2.2
    @PostMapping
    public ResponseEntity<TokenResponse> generateToken(@Valid @RequestBody TokenRequest request) { ... }
}
```

Move `convertHexStringToReversedByteArray()` out of `StsApplication` into a package-private
utility method in a new `StsUtils.java` file (or inline it where used).

**Files to create/modify:**
- `StsApplication.java` — stripped down to bootstrap only
- `TokenController.java` — new file
- `TokenRequest.java` — new file (replaces `RequestData.java` — see Task 2.3)
- `TokenResponse.java` — new file (`{ "token": "..." }` response shape)
- `StsUtils.java` — new file for `convertHexStringToReversedByteArray()`

**Done when:** `POST /token` behaves identically to before. `StsApplication` has no
`@RestController` annotation.

---

### Task 2.2 — Introduce `TokenStrategy` interface and move dispatch logic
- [ ] **Status:** Not started
- **Depends on:** 2.1

**Current state:**
The four token types are dispatched via `if/else` in `MyApplication.home()`. Each branch
constructs domain objects and instantiates a generator directly. Unknown types return `null`.

**Target state:**
Create `TokenStrategy.java`:
```java
public interface TokenStrategy {
    /** Returns true if this strategy handles the given token type. */
    boolean supports(TokenType type);

    /** Generates the 20-digit STS token string for the given request. */
    String generate(TokenRequest request) throws Exception;
}
```

Create four `@Component` implementations, one per token type:
- `TransferElectricityCreditStrategy` — handles `TOP_UP`
- `ClearCreditStrategy` — handles `CLEAR_CREDIT`
- `ClearTamperStrategy` — handles `CLEAR_TAMPER`
- `SetMaximumPowerLimitStrategy` — handles `SET_POWER_LIMIT`

Each implementation contains exactly the construction and generator call that was previously
inline in the `if/else` branch. Named constants replace the magic values:
```java
// STS IEC 62055-41 §10: base date 2014 is the reference epoch for this token generation
private static final BaseDate STS_BASE_DATE = BaseDate._2014;
// Maximum key expiry: value 255 means "no expiry" per the STS standard
private static final int KEY_EXPIRY_NO_EXPIRY = 255;
```

`TokenController` injects `List<TokenStrategy>` via Spring and dispatches:
```java
TokenStrategy strategy = strategies.stream()
    .filter(s -> s.supports(request.getType()))
    .findFirst()
    .orElseThrow(() -> new UnsupportedTokenTypeException(request.getType()));
```

**Files to create:**
- `strategy/TokenStrategy.java`
- `strategy/TransferElectricityCreditStrategy.java`
- `strategy/ClearCreditStrategy.java`
- `strategy/ClearTamperStrategy.java`
- `strategy/SetMaximumPowerLimitStrategy.java`

> **`sts-core` boundary check:** all five files above live in `co.nxtgrid.strategy.*` (wrapper
> layer). They may import from `co.nxtgrid.token.*` freely but must not import
> `org.springframework.*` beyond `@Component`. Run the invariant verification grep after this
> task to confirm no Spring imports crept into `co.nxtgrid.token.*` or `co.nxtgrid.ca.*`.

**Done when:** all four token types produce the same tokens as before (verified by test vectors
added in Task 2.5). Unknown type strings return HTTP 400, not HTTP 200 with null body.
`grep -r "import org.springframework" src/main/java/co/nxtgrid/token/` returns no results.

---

### Task 2.3 — Add input validation to `TokenRequest`
- [ ] **Status:** Not started
- **Depends on:** 2.1

**Current state:**
`RequestData.java` has no validation annotations. `spring-boot-starter-validation` is already
a dependency. Invalid inputs (null decoder key, out-of-range random number, etc.) either cause
an unhandled exception or silently produce a wrong token.

**Target state:**
Replace `RequestData.java` with `TokenRequest.java` (matching Task 2.1) that uses Bean
Validation annotations:

```java
public class TokenRequest {
    @NotNull
    @Pattern(regexp = "^[0-9A-Fa-f]{16}$", message = "decoderKey must be exactly 16 hex characters")
    private String decoderKey;

    @NotNull
    private TokenType type;  // enum — invalid values caught at deserialization

    @NotNull
    private LocalDateTime issueDate;

    @Min(0) @Max(15)
    private int randomNumber;  // STS 4-bit RND — see Task 2.7 for @Schema documentation

    // Conditionally required: kwh required for TOP_UP, powerLimit required for SET_POWER_LIMIT
    private Double kwh;
    private Long powerLimit;
}
```

Add `@Valid` to the controller parameter. Invalid requests return HTTP 400 with a structured
error body (handled by the `@RestControllerAdvice` in Task 2.4).

Also replace the Joda-Time `DateTime` deserializer with `java.time.LocalDateTime` (supported
natively by Jackson in Spring Boot 3.x). Remove the `DateTimeConverter.java` component and
the Joda-Time `setIssueDate(String)` workaround in `RequestData`. The Joda-Time dependency
can be retained for the generator domain classes for now, but the API layer should not depend
on it.

**Files to modify/create:**
- `TokenRequest.java` — new file (replaces `RequestData.java`)
- `RequestData.java` — delete
- `DateTimeConverter.java` — delete
- Each `TokenStrategy` implementation — update to accept `LocalDateTime` and convert to
  Joda `DateTime` internally where the generator requires it

**Done when:**
- `POST /token` with a 15-character decoder key returns HTTP 400 with `{"error": "...", "field": "decoderKey"}`.
- `POST /token` with `"type": "INVALID"` returns HTTP 400.
- `POST /token` with `"randomNumber": 16` returns HTTP 400 with a message indicating 0–15.
- `POST /token` with `"randomNumber": 125489697135` returns HTTP 400 (JSON parse / validation
  error, not an empty body).

---

### Task 2.4 — Add `@RestControllerAdvice` error handling
- [ ] **Status:** Not started
- **Depends on:** 2.1

**Current state:**
The `try/catch` in `MyApplication.home()` calls `e.printStackTrace()` and returns `null`,
which Spring serializes as HTTP 200 with an empty body. There is no structured error response.
Malformed JSON (e.g. a `randomNumber` value outside Java `int` range) yields
`HttpMessageNotReadableException` with no friendly body. Domain failures such as
`InvalidRangeException` from `RandomNo` are caught and swallowed the same way.

**Target state:**
Create `StsExceptionHandler.java`:
```java
@RestControllerAdvice
public class StsExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) { ... }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMalformedJson(HttpMessageNotReadableException ex) { ... }

    @ExceptionHandler(InvalidRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainRange(InvalidRangeException ex) { ... }

    @ExceptionHandler(UnsupportedTokenTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupportedType(UnsupportedTokenTypeException ex) { ... }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected(Exception ex) { ... }
}
```

Create `ErrorResponse.java` (`{ "error": "message", "field": "optional field name" }`).
Create `UnsupportedTokenTypeException.java`.

Remove the `try/catch` from the controller; let exceptions propagate to the advice.
Replace `e.printStackTrace()` with `log.error("Token generation failed", e)` using SLF4J
(`LoggerFactory.getLogger(TokenController.class)`).

`HttpMessageNotReadableException` handler should produce a user-facing message for common cases
(e.g. "randomNumber must be an integer between 0 and 15" when the root cause is numeric
overflow on that field).

**Files to create:**
- `StsExceptionHandler.java`
- `ErrorResponse.java`
- `UnsupportedTokenTypeException.java`

**Done when:**
- `POST /token` with a valid request returns HTTP 200 `{"token": "..."}`.
- `POST /token` with bad input returns HTTP 400 `{"error": "..."}`.
- `POST /token` with `randomNumber` out of int range returns HTTP 400 JSON (not whitelabel HTML).
- `POST /token` with `randomNumber` 16+ returns HTTP 400 JSON.
- `POST /token` with a decoder key that triggers an unexpected crypto exception returns HTTP 500 `{"error": "..."}`.
- No response ever has a null or empty body.

---

### Task 2.5 — Add `TokenType` enum
- [ ] **Status:** Not started
- **Depends on:** nothing (can run in parallel)

**Current state:**
Token types are compared as raw `String` values in the `if/else` chain.

**Target state:**
```java
public enum TokenType {
    /** Class 0 — transfers electricity credit to the meter. */
    TOP_UP,
    /** Class 2 — clears existing credit balance on the meter. */
    CLEAR_CREDIT,
    /** Class 2 — clears the tamper condition flag on the meter. */
    CLEAR_TAMPER,
    /** Class 2 — sets the maximum power draw limit on the meter. */
    SET_POWER_LIMIT;
}
```

Jackson deserializes unrecognized strings as `null` by default; configure
`DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES` or annotate with `@JsonProperty` to
return a clean 400 on unknown type values.

**Files to create:**
- `TokenType.java`

**Done when:** `TokenRequest.type` is `TokenType`; `TokenStrategy.supports(TokenType)` uses
the enum; `grep -r '"TOP_UP"' src/` returns zero results outside of tests.

---

### Task 2.6 — Add JUnit tests with known STS token vectors
- [ ] **Status:** Not started
- **Depends on:** 2.2

**Current state:**
`src/test/` does not exist. There are zero tests. This means any change to the crypto path
has no regression protection.

**Target state:**
Create `src/test/java/co/nxtgrid/` and add at minimum:
- `TokenStrategyIntegrationTest.java` — one `@SpringBootTest` test per token type that posts
  a known request and asserts the exact expected 20-digit token string. The test vectors must
  be derived from a trusted reference (run the current working service and record output, or
  cross-reference with the NectarAPI project's own test vectors if available).
- `TokenControllerValidationTest.java` — uses `MockMvc` to assert HTTP 400 for each
  invalid input scenario (bad hex length, out-of-range random, unknown type, missing required
  field, numeric overflow on `randomNumber`).
- `RootControllerTest.java` — asserts `GET /` returns HTTP 200 JSON with service name and
  endpoint links.

Example vector structure:
```java
// Vector sourced from current production service output, 2026-07-02
// decoderKey: 1234567890ABCDEF | issueDate: 2024-03-15T10:30:00 | rnd: 3 | kwh: 50.0
assertThat(token).isEqualTo("12345678901234567890");
```

**Files to create:**
- `src/test/java/co/nxtgrid/TokenStrategyIntegrationTest.java`
- `src/test/java/co/nxtgrid/TokenControllerValidationTest.java`
- `src/test/java/co/nxtgrid/RootControllerTest.java`

**Done when:** `mvn test` passes. At least four token-vector tests (one per type) and at least
five validation rejection tests are green. `RootControllerTest` passes.

---

### Task 2.7 — Add OpenAPI / Swagger UI documentation
- [ ] **Status:** Not started
- **Depends on:** 2.3, 2.4

**Current state:**
No OpenAPI dependency or annotations. Integrators must read `README.md` or Java source to
learn request field constraints. The `randomNumber` field (STS 4-bit RND, range 0–15) is
especially easy to misuse.

**Target state:**
Add to `pom.xml` (version managed by Spring Boot parent where possible, or pin explicitly):
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version>
</dependency>
```

Annotate wrapper-layer DTOs with `io.swagger.v3.oas.annotations.media.Schema`. The field name
**stays `randomNumber`** — do not rename. Example for that field:

```java
@Schema(
    description = "STS RND field (4 bits). Must be an integer from 0 to 15. "
        + "Vary between token issues to avoid duplicate-token rejection on the meter. "
        + "This is not a meter serial number or other large identifier.",
    minimum = "0",
    maximum = "15",
    example = "3"
)
@Min(0) @Max(15)
private int randomNumber;
```

Apply `@Schema` to all `TokenRequest` fields, `TokenResponse`, and `ErrorResponse`. Add an
`@Operation` summary on `TokenController.generateToken()`.

Optional `OpenApiConfig.java` bean to set API title (`NXT STS`), version (from `pom.xml` /
`spring.application.name`), and description.

Expose (springdoc defaults):
- `GET /swagger-ui.html` — interactive Swagger UI
- `GET /v3/api-docs` — OpenAPI 3 JSON

**Files to create/modify:**
- `pom.xml` — add springdoc dependency
- `TokenRequest.java`, `TokenResponse.java`, `ErrorResponse.java` — `@Schema` annotations
- `TokenController.java` — `@Operation` / `@ApiResponse` annotations
- `OpenApiConfig.java` — optional API metadata bean

**Done when:**
- `GET /swagger-ui.html` loads and documents `POST /token` with all fields.
- `randomNumber` shows `minimum: 0`, `maximum: 15`, and the STS RND description in Swagger UI.
- `GET /v3/api-docs` returns valid OpenAPI JSON including the same constraints.

---

### Task 2.8 — Add JSON root route (`GET /`)
- [ ] **Status:** Not started
- **Depends on:** 2.1

**Current state:**
`GET /` returns Spring Boot's whitelabel HTML error page (404). There is no service discovery
endpoint for operators visiting the base URL.

**Target state:**
Create `RootController.java`:
```java
@RestController
public class RootController {

    @GetMapping("/")
    public ServiceInfo index() { ... }
}
```

`ServiceInfo` is a simple JSON DTO (wrapper layer):
```json
{
  "name": "nxt-sts",
  "version": "1.0.0",
  "description": "IEC 62055-41 STS prepayment token generation service",
  "endpoints": {
    "token": "POST /token",
    "health": "GET /actuator/health",
    "openapi": "GET /v3/api-docs",
    "swaggerUi": "GET /swagger-ui.html"
  }
}
```

Read `version` from `@Value("${project.version:unknown}")` or `build-info` / `pom` property
injected via `application.properties` (`info.app.version=${project.version}`) — pick one
approach and document it in the README.

**Files to create:**
- `RootController.java`
- `ServiceInfo.java` (or inline `Map` if preferred — a typed DTO is clearer for OpenAPI)

**Done when:**
- `GET /` returns HTTP 200 JSON (not HTML).
- Response includes links to `/token`, `/actuator/health`, `/v3/api-docs`, and `/swagger-ui.html`.
- `RootControllerTest` (Task 2.6) passes.

---

### Phase 2 checkpoint

- `mvn verify` (build + test) passes from a clean checkout.
- `curl http://localhost:8080/` returns JSON service index (not whitelabel HTML).
- `curl http://localhost:8080/swagger-ui.html` loads Swagger UI.
- `curl -X POST http://localhost:8080/token -H "Content-Type: application/json" -d '{"type":"TOP_UP","issueDate":"2024-03-15T10:30:00","randomNumber":3,"decoderKey":"1234567890ABCDEF","kwh":50.0}'` returns HTTP 200 with a 20-digit token.
- Every known error path returns a structured JSON error with the correct HTTP status code.
- No null or empty response body is possible from any code path.

---

## Phase 3 — Deployment and OSS scaffolding

**Goal:** a contributor can fork, build, and run the service using only the repository and
Docker. A CI pipeline guards `main`. Tagged releases produce container images automatically.

**Estimated effort:** ~4–5 days

---

### Task 3.1 — Rewrite `Dockerfile` as multi-stage
- [ ] **Status:** Not started
- **Depends on:** 1.6 (correct artifact name)

**Current state:**
```dockerfile
FROM eclipse-temurin as builder
WORKDIR /app
COPY target/jambu-1.0-SNAPSHOT.jar jambu-1.0-SNAPSHOT.jar
CMD ["java", "-jar", "jambu-1.0-SNAPSHOT.jar"]
```

Problems: the stage is named "builder" but is actually a runtime stage (no build happens);
the JAR is copied from the host's `target/` (requires pre-built artifact); image runs as root;
no tag pin; no `EXPOSE`; no `HEALTHCHECK`.

**Target state:**
```dockerfile
# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -q
COPY src/ src/
RUN ./mvnw package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine AS runtime
RUN addgroup -S sts && adduser -S sts -G sts
WORKDIR /app
COPY --from=build /app/target/nxt-sts-*.jar app.jar
USER sts
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Also add `.dockerignore`:
```
target/
.git/
.github/
*.md
.DS_Store
.idea/
```

**Done when:** `docker build -t nxt-sts .` succeeds from a clean clone (no `target/` present).
`docker run -p 8080:8080 nxt-sts` starts the service. The health endpoint responds.

---

### Task 3.2 — Add Maven wrapper
- [ ] **Status:** Not started
- **Depends on:** nothing

**Target state:**
Run `mvn wrapper:wrapper` (Maven Wrapper Plugin) in the project root. This generates:
- `mvnw` (Unix shell script)
- `mvnw.cmd` (Windows batch script)
- `.mvn/wrapper/maven-wrapper.properties`

Pin the Maven version in `maven-wrapper.properties` (use the same version available in the
CI runner — check GitHub Actions `ubuntu-latest` default or pin explicitly to `3.9.x`).

Commit `mvnw`, `mvnw.cmd`, and `.mvn/`.

**Done when:** `./mvnw clean package -DskipTests` succeeds on a machine with no local Maven
installation (only the JDK).

---

### Task 3.3 — Add Spring Boot Actuator and committed config
- [ ] **Status:** Not started
- **Depends on:** nothing

**Current state:**
No `src/main/resources/` directory exists. No health endpoint is available. The Dockerfile's
`HEALTHCHECK` (added in Task 3.1) references `/actuator/health` which will 404 without this
task.

**Target state:**
Add to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Create `src/main/resources/application.properties`:
```properties
server.port=8080
server.error.whitelabel.enabled=false
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=never
spring.application.name=nxt-sts
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

`server.error.whitelabel.enabled=false` ensures unknown routes return JSON error responses
(via `@RestControllerAdvice` or Spring's default JSON error handler) instead of HTML whitelabel
pages. Verified in Task 2.4 / Phase 2 checkpoint.

**Done when:** `GET /actuator/health` returns `{"status":"UP"}`.

---

### Task 3.4 — Add GitHub Actions CI pipeline
- [ ] **Status:** Not started
- **Depends on:** 3.2 (Maven wrapper must exist for CI to use it)

**Current state:** `.github/` does not exist.

**Target state:**
Create `.github/workflows/build.yml`:
```yaml
name: Build and Test
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: 'maven'
      - run: ./mvnw verify
```

Create `.github/workflows/release.yml`:
```yaml
name: Release
on:
  push:
    tags: ['v*.*.*']
jobs:
  publish:
    runs-on: ubuntu-latest
    permissions:
      packages: write
      contents: read
    steps:
      - uses: actions/checkout@v4
      - uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}
      - uses: docker/build-push-action@v5
        with:
          push: true
          tags: ghcr.io/${{ github.repository }}:${{ github.ref_name }},ghcr.io/${{ github.repository }}:latest
```

**Done when:** a PR to `main` triggers the build workflow and `mvn verify` passes in CI.
Pushing a `v1.0.0` tag triggers the release workflow and publishes the image to GHCR.

---

### Task 3.5 — Update README and CONTRIBUTING for the new structure
- [ ] **Status:** Not started
- **Depends on:** 3.1, 3.2, 3.3, 3.4, 2.2, 2.7, 2.8

**Current state:**
The README is reasonably written but has several gaps now that the structure has changed:
- Build instructions reference `mvn` (not `./mvnw`) and the old JAR name `jambu-1.0-SNAPSHOT.jar`.
- The Dockerfile section requires a pre-built JAR; the new multi-stage build does not.
- There is no environment variable reference table.
- There is no "how to add a token type" section.
- `CONTRIBUTING.md` describes the process but not the technical steps for adding a token type.
- No mention of Swagger UI, the JSON root route, or detailed `randomNumber` field guidance.

**Target state:**
Update `README.md`:
1. Replace `mvn clean install -DskipTests` with `./mvnw clean package -DskipTests` throughout.
2. Update Docker section: `docker build -t nxt-sts .` then `docker run -p 8080:8080 nxt-sts`
   (no pre-build step needed).
3. Add a **Configuration** section listing all supported environment variables / properties
   with their defaults and descriptions.
4. Add a **CI / Container image** section describing the GitHub Actions pipeline and GHCR image.
5. Add a **Supported token types** section that matches the current four types plus a short
   note on how to add a new one (link to `CONTRIBUTING.md`).
6. Add an **API documentation** section:
   - `GET /` — JSON service index
   - `GET /swagger-ui.html` — interactive OpenAPI docs (preferred reference for integrators)
   - `GET /v3/api-docs` — OpenAPI JSON
   - `GET /actuator/health` — health check
7. Expand the **`randomNumber`** field documentation in the API Reference table:
   - STS 4-bit RND field; **must be 0–15** (protocol constraint, not an arbitrary API limit)
   - Not a meter serial number or large identifier
   - Vary between token issues to avoid duplicate-token rejection on the meter
   - Link to Swagger UI for the full schema

Update `CONTRIBUTING.md`:
Add a **"Adding a token type"** section:
```
1. Create a class in `src/main/java/co/nxtgrid/strategy/` that implements `TokenStrategy`.
2. Annotate it with `@Component` — Spring picks it up automatically.
3. Implement `supports(TokenType type)` and `generate(TokenRequest request)`.
4. Add a test vector to `TokenStrategyIntegrationTest`.
5. Document the new type in the API Reference table in README.md.
```

**Done when:** `README.md` quick-start works end-to-end on a fresh clone without any
context from this document. `CONTRIBUTING.md` explains how to add a token type concretely.
README links to Swagger UI and documents `randomNumber` constraints clearly.

---

### Phase 3 checkpoint

- `git clone` → `./mvnw verify` → `docker build` → `docker run` → `curl POST /token` works
  entirely from the README quick-start instructions, with no prior knowledge.
- A PR to `main` shows a green CI check.
- `GET /actuator/health` returns `{"status":"UP"}`.

---

## Phase 4 (future) — Core library extraction

**Deferred per ADR-001 decision 7.** Track separately when there is a concrete consumer
(another JVM service wanting to embed STS generation without an HTTP call).

If the architectural invariant (see above) was respected throughout phases 1–3, Phase 4 is
purely a Maven packaging and publishing exercise — no code logic needs to change.

Work items:
- **4.1 Split into multi-module Maven project:** create a `sts-core` module (no Spring,
  only BouncyCastle + Joda-Time) and an `sts-service` module (Spring Boot, depends on
  `sts-core`). The four `TokenStrategy` implementations move to `sts-core`; the HTTP layer
  stays in `sts-service`.
- **4.2 Publish `sts-core` to Maven Central:** configure the Maven release plugin and
  Sonatype OSSRH publishing. Requires a `pom.xml` with `<scm>`, `<developers>`, and
  `<distributionManagement>` sections.
- **4.3 Conformance test vector suite:** extract the STS test vectors from the JUnit tests
  into a standalone JSON file (`test-vectors.json`) with a format that can be consumed by
  test suites in any language. This is the prerequisite for any multi-language port.

## Phase 5 (future) — Multi-language ports

**Deferred per ADR-001 decision 7.** Each port is a separate repository and effort.
Prerequisite: Phase 4.3 (conformance test vectors) must be complete before any port is started.

Work items per language:
- Implement STA/EA07 (DES with the STS permutation/substitution tables) using the language's
  cryptographic primitives.
- Pass all vectors in `test-vectors.json`.
- Publish to the language's package registry (npm, Packagist, PyPI).
- Cross-reference from the main `nxt-sts` README.

---

## Notes & decisions log

> Append here as the plan is executed. Format: `YYYY-MM-DD — [task id] — note`

_(empty)_
