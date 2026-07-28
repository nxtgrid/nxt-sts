# docs/ — index for agents

Read this file first. Then load only the document(s) relevant to your task.

| File | What it contains | Read when |
|---|---|---|
| `docs/capabilities.md` | Full matrix of supported STS token types, which are REST-exposed, and how to add a new one | Adding or researching a token type |
| `docs/plans/` | Engineering execution plans (one file per effort). Each file lists tasks with status, done criteria, and notes. Plan 001 Phases 4–5 cover deferred `sts-core` extraction and language ports. | Executing a planned task or checking what has already been done |
| `docs/architecture/` | Architecture Decision Records (ADRs). ADR-001 decision 7 defines the `sts-core` package boundary (`co.nxtgrid.token.*` = no Spring). ADR-001 risks also note the planned BouncyCastle `jdk18on` upgrade. | Making a structural change, adding a dependency, touching `token/`, or understanding a boundary rule |
| `CONTRIBUTING.md` → **Releasing** | Version bump checklist (`pom.xml` only), tagging, GHCR verification | Bumping the project version or cutting a release |

Public-facing summary of the same boundary and deferred work: `README.md` → Architecture / Roadmap.

**Do not read all docs upfront.** Load `docs/capabilities.md` only when working on token types. Load a plan file only when executing tasks from it. Load an ADR only when the task touches architecture or constraints described in it. Load `CONTRIBUTING.md` (Releasing) only when versioning or releasing.
