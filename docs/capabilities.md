# NXT STS — token capabilities

This document maps **IEC 62055-41 (STS)** token types to what exists in the codebase today.

| Column | Meaning |
|---|---|
| **API `type`** | String sent in `POST /token` JSON (`type` field). `—` means no REST type is defined yet. |
| **STS class / subclass** | Token class and 4-bit subclass encoded in the 20-digit token (software `nativetoken` path). |
| **Generate** | A `nativetoken` generator exists under `token/generators/tokensgenerator/nativetoken/`. |
| **Decode** | A decoder exists under `token/generators/tokensdecoder/` (library only — no HTTP endpoint). |
| **REST API** | Exposed via `POST /token` in the running service. |

All generators on the live path use the **Standard Transfer Algorithm (STA / EA07)** with a caller-supplied decoder key.

---

## Token generation and decode

| API `type` | STS class / subclass | Description | Generate | Decode | REST API |
|---|---|---|:---:|:---:|:---:|
| `TOP_UP` | 0 / 0 | Transfer electricity credit (kWh) | Yes | Yes | **Yes** |
| — | 0 / 1 | Transfer water credit | Yes | Yes | No |
| — | 0 / 2 | Transfer gas credit | Yes | Yes | No |
| — | 0 / 3 | Time token | No | No | No |
| — | 0 / 4 | Transfer electricity credit in currency units | Yes | No | No |
| — | 1 / 0 | Initiate meter test or display 1 | Yes | Yes | No |
| — | 1 / 1 | Initiate meter test or display 2 | Yes | Yes | No |
| `SET_POWER_LIMIT` | 2 / 0 | Set maximum power limit | Yes | Yes | **Yes** |
| `CLEAR_CREDIT` | 2 / 1 | Clear credit | Yes | Yes | **Yes** |
| — | 2 / 2 | Set tariff rate | Yes | Yes | No |
| — | 2 / 3 | Set 1st section decoder key (key change) | Yes | Yes | No |
| — | 2 / 4 | Set 2nd section decoder key (key change) | Yes | Yes | No |
| `CLEAR_TAMPER` | 2 / 5 | Clear tamper condition | Yes | Yes | **Yes** |
| — | 2 / 6 | Set maximum phase power unbalance limit | Yes | Yes | No |
| — | 2 / 7 | Set water meter factor | Yes | Yes | No |
| — | 2 / 8 | Set 3rd section decoder key (key change) | Yes | Yes | No |
| — | 2 / 9 | Set 4th section decoder key (key change) | Yes | Yes | No |

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
| `token/generators/tokensgenerator/prism/` | Parallel generators that route through a Prism HSM. Not called from the live service; scheduled for removal (open-source plan Task 1.3). |
| `hsm/prism/` | Thrift client for Prism HSM. Not wired into Spring; scheduled for removal (Task 1.2). |
| `co.nxtgrid.tokens.*` | Original NectarAPI service orchestration layer. Removed (Task 1.1). |

---

## Adding a new REST token type

Only four types are wired to `POST /token` today. To expose another row from the table above, add a `TokenStrategy` implementation (planned in open-source preparation Phase 2) that constructs the domain objects and calls the existing `nativetoken` generator. See [CONTRIBUTING.md](../CONTRIBUTING.md) and the engineering plan for details.
