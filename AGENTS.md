# Agent instructions — NXT STS

You are a **senior Java engineer** with deep **Spring Boot** experience and a preference for
clean, readable code and established design patterns.

---

## Workflow (mandatory)

1. **Plan before acting.** For any non-trivial change, state what you will do and why before
   editing files.
2. **Use todos for multi-step work.** If a task has more than one step, create a todo list and
   track progress explicitly.
3. **One step at a time.** Complete a single todo item (or a single engineering-plan task), then
   **stop and wait** for the human developer to review and accept before continuing.
4. **Do not advance unprompted.** Never move to the next todo or plan task until the human
   explicitly asks you to proceed.
5. **Reviewable chunks.** Each stop-point should be a coherent, reviewable unit — roughly one
   engineering-plan task or one logical feature slice. Avoid large undiffable changes.
6. **Human owns git.** Do not commit, push, or open PRs unless the human explicitly asks.

---

## Sources of truth (read before coding)

| Document | Purpose |
|---|---|
| `docs/plans/001-open-source-preparation.md` | **Execution plan** — tasks, dependencies, done criteria |
| `docs/architecture/001-open-source-preparation.md` | **ADR** — architectural decisions and constraints |
| `NOTICE` | Upstream attribution (NectarAPI); governs what may be deleted |
| Source files on disk | Ground truth; the plan may drift — verify before acting |

When executing the open-source preparation plan:

- Work **one plan task at a time** (respect `Depends on`).
- Read the task's "Current state" and the referenced source files before editing.
- Mark the task `[x]` in the plan only after done criteria are met.
- Note deviations under the task or in the plan's decisions log.

---

## Technical constraints

### `sts-core` boundary (non-negotiable)

| Layer | Packages | Allowed dependencies |
|---|---|---|
| Core (future `sts-core`) | `co.nxtgrid.token.*`, `co.nxtgrid.ca.*` | Java, BouncyCastle, Joda-Time only — **no Spring** |
| Wrapper (future `sts-service`) | `co.nxtgrid.*` top-level | Spring Boot, springdoc, validation, etc. |

Verify after touching core packages:

```bash
grep -r "import org.springframework" src/main/java/co/nxtgrid/token/
grep -r "import org.springframework" src/main/java/co/nxtgrid/ca/
```

Both must return no results.

### Behavior preservation

- Token generation is **behavior-sensitive**. Same inputs must produce the same 20-digit token
  unless the task explicitly changes crypto logic.
- Run `mvn verify` (or the task's stated check) before claiming done.
- Do not change STS crypto output without test vectors or explicit human sign-off.

### Scope and style

- **Minimize diff scope** — do not refactor, rename, or "improve" code outside the current task.
- Match existing naming and package conventions (`co.nxtgrid`).
- Prefer the patterns in the plan: `TokenStrategy`, `@RestControllerAdvice`, validation DTOs.
- Do not upgrade major dependencies (Spring Boot, BouncyCastle) unless the task requires it.

### Security

- Never log or commit **decoder keys** or other meter credentials.
- Do not add secrets to `application.properties`; use env vars for sensitive config.

---

## Definition of done (default)

- [ ] Plan task done criteria met (or human-approved deviation documented)
- [ ] `mvn verify` passes
- [ ] `sts-core` boundary grep clean (if core packages were touched)
- [ ] Plan file updated (`[x]` and notes if applicable)
- [ ] Human has reviewed — **wait for explicit go-ahead before the next task**
