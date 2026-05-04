# Contributing to NXT STS

Thank you for your interest in contributing. This document explains how to participate effectively.

---

## Branch and Merge Policy

The `main` branch is **production-protected**:

- Direct pushes to `main` are restricted to the core maintainers (see [AUTHORS.md](AUTHORS.md)).
- All changes — including those from maintainers — must go through a **pull request** and receive at least **one approval** before merging.
- This constraint exists because `main` is the build source for a live production service.

---

## How to Contribute

1. **Fork** the repository on GitHub.
2. **Create a feature branch** from `main` in your fork:
   ```
   git checkout -b feature/your-feature-name
   ```
   Use a descriptive prefix: `feature/`, `fix/`, `docs/`, `refactor/`.
3. **Make your changes** — keep commits focused and well-described.
4. **Ensure the project still builds** before opening a PR:
   ```
   mvn clean install -DskipTests
   ```
5. **Open a pull request** against `main` in the upstream repository.
   - Write a clear PR title and description explaining the *why*, not just the *what*.
   - Reference any related issues (e.g. `Closes #12`).
6. A maintainer will review your PR. Address any feedback and push follow-up commits to the same branch.

---

## Reporting Issues

- Search existing issues before opening a new one.
- Include enough context to reproduce the problem: Java version, request payload, observed vs expected output.
- For security-sensitive issues (e.g. cryptographic concerns), contact the maintainers directly by email rather than opening a public issue. See [AUTHORS.md](AUTHORS.md) for email addresses.

---

## Code Style

- Java 17+ language features are welcome.
- Follow the existing package structure (`co.nxtgrid.*`).
- Do not commit IDE or OS-specific files — `.vscode/`, `.DS_Store`, etc. are already git-ignored.
- Do not commit build artifacts. The `target/` directory is git-ignored.
- Keep decoder keys, meter credentials, and any other secrets out of source files — use environment variables or external secret management in deployment.

---

## Adding Yourself as a Contributor

If your pull request is merged, you are welcome to add your name and email (or GitHub handle) to [CONTRIBUTORS.md](CONTRIBUTORS.md) in the same PR.

---

## License

By submitting a pull request you agree that your contribution will be licensed under the [GNU Affero General Public License v3.0](LICENSE), the same license that covers this project.
