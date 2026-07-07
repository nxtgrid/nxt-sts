# docs/ — index for agents

Read this file first. Then load only the document(s) relevant to your task.

| File | What it contains | Read when |
|---|---|---|
| `docs/capabilities.md` | Full matrix of supported STS token types, which are REST-exposed, and how to add a new one | Adding or researching a token type |
| `docs/plans/` | Engineering execution plans (one file per effort). Each file lists tasks with status, done criteria, and notes. | Executing a planned task or checking what has already been done |
| `docs/architecture/` | Architecture Decision Records (ADRs). Each file captures the *why* behind structural decisions and the constraints that follow. | Making a structural change, adding a dependency, or understanding a boundary rule |

**Do not read all docs upfront.** Load `docs/capabilities.md` only when working on token types. Load a plan file only when executing tasks from it. Load an ADR only when the task touches architecture or constraints described in it.
