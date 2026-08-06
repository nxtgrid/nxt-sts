# NXT STS — token capabilities

This document maps **IEC 62055-41 (STS)** token types to what exists in the codebase today.

| Column | Meaning |
|---|---|
| **API `type`** | String sent in `POST /token` JSON (`type` field). `—` means no REST type is defined yet. |
| **STS class / subclass** | Token class and 4-bit subclass encoded in the 20-digit token (software `nativetoken` path). |
| **Generate** | A `nativetoken` generator exists under `token/generators/tokensgenerator/nativetoken/`. |
| **Decode** | Standalone decode orchestration (`tokensdecoder/` package). Removed in Task 1.3; a future `POST /decode` would need new wiring. Domain `Token.decode()` helpers remain. |
| **REST API** | Exposed via `POST /token` in the running service. |

All generators on the live path use the **Standard Transfer Algorithm (STA / EA07)** with a caller-supplied decoder key.

**Deprecated wire alias:** `TOP_UP` is still accepted on `POST /token` and is normalized to
`TOP_UP_KWH` at deserialize time (same strategy / generator path). Prefer `TOP_UP_KWH` for new
callers; remove `TOP_UP` once older servers have been updated.

---

## Token generation and decode

| API `type` | STS class / subclass | Description | Generate | Decode | REST API |
|---|---|---|:---:|:---:|:---:|
| `TOP_UP_KWH` | 0 / 0 | Transfer electricity credit (kWh) | Yes | — | **Yes** |
| — | 0 / 1 | Transfer water credit | Yes | — | No |
| — | 0 / 2 | Transfer gas credit | Yes | — | No |
| — | 0 / 3 | Time token | No | No | No |
| — | 0 / 4 | Transfer electricity credit in currency units | Yes | — | No |
| — | 1 / 0 | Initiate meter test or display 1 | Yes | — | No |
| — | 1 / 1 | Initiate meter test or display 2 | Yes | — | No |
| `SET_POWER_LIMIT` | 2 / 0 | Set maximum power limit | Yes | — | **Yes** |
| `CLEAR_CREDIT` | 2 / 1 | Clear credit | Yes | — | **Yes** |
| — | 2 / 2 | Set tariff rate | Yes | — | No |
| — | 2 / 3 | Set 1st section decoder key (key change) | Yes | — | No |
| — | 2 / 4 | Set 2nd section decoder key (key change) | Yes | — | No |
| `CLEAR_TAMPER` | 2 / 5 | Clear tamper condition | Yes | — | **Yes** |
| — | 2 / 6 | Set maximum phase power unbalance limit | Yes | — | No |
| — | 2 / 7 | Set water meter factor | Yes | — | No |
| — | 2 / 8 | Set 3rd section decoder key (key change) | Yes | — | No |
| — | 2 / 9 | Set 4th section decoder key (key change) | Yes | — | No |

**Class 3** tokens have an abstract domain base (`Class3Token`) only; there is no generator or decoder.

**Time token (class 0 / subclass 3)** has a subclass definition in the domain model but no generator or decoder implementation.

---

## Decoder key generation (DKGA)

Separate from 20-digit vending tokens: algorithms to derive a meter decoder key from vending keys and meter parameters. Used internally when generating keys; not exposed via `POST /token`.

| DKGA | Implementation | REST API |
|---|---|:---:|
| `01` | `DecoderKeyGeneratorAlgorithm01` | No |
| `02` | `DecoderKeyGeneratorAlgorithm02` | No |
| `03` | `DecoderKeyGeneratorAlgorithm03` | No |
| `04` | `DecoderKeyGeneratorAlgorithm04` | No |

---

## What is not in this table

The repository also contains code that is **not** part of the software STA path documented above:

| Path | Status |
|---|---|
| `token/generators/tokensgenerator/prism/` | Removed (Task 1.2). |
| `hsm/prism/` | Removed (Task 1.2). |
| `co.nxtgrid.tokens.*` | Removed (Task 1.1). |
| `token/generators/tokensdecoder/` | Removed (Task 1.3). |

---

## Adding a new REST token type

Only four types are wired to `POST /token` today. To expose another row from the table above, add a `TokenStrategy` implementation in `co.nxtgrid.strategy.*` that constructs the domain objects and calls the existing `nativetoken` generator. Strategies are wrapper-layer code (HTTP → domain); the generators themselves live in Spring-free `co.nxtgrid.token.*`. See [CONTRIBUTING.md](../CONTRIBUTING.md) and [ADR-001](architecture/001-open-source-preparation.md) for the package boundary.
