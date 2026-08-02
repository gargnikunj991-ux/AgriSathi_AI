# 🤝 Contributing to AgriSathi AI

Thank you for taking the time to contribute to **AgriSathi AI**! We welcome all contributions from bug fixes and feature implementations to documentation improvements.

---

## 1. Branching Strategy

We follow a structured **Git Flow** strategy:

- `main` / `master` — Production-ready stable branch.
- `backend` — Active backend development branch.
- `feature/<feature-name>` — New feature implementation branch (e.g., `feature/crop-disease-v2`).
- `fix/<bug-name>` — Bug fix branch (e.g., `fix/jwt-expiration-handling`).
- `docs/<doc-name>` — Documentation updates branch (e.g., `docs/update-api-contract`).

---

## 2. Commit Message Guidelines

We follow the **Conventional Commits** format for clear git commit histories:

```
<type>(<scope>): <short descriptive summary>

[optional body describing why the change was made]
```

### Supported Types:
- `feat`: A new feature for the user or system
- `fix`: A bug fix
- `docs`: Documentation only changes
- `style`: Formatting, missing semi-colons, whitespace fixes (no code logic changes)
- `refactor`: Refactoring production code (e.g. renaming a variable or method)
- `test`: Adding missing tests or refactoring existing tests
- `chore`: Maintenance tasks, dependency updates, build configuration

### Example Commit Messages:
```bash
git commit -m "feat(disease-scan): add image validation for crop upload"
git commit -m "fix(auth): handle expired JWT tokens gracefully"
git commit -m "docs(api): update API contract for marketplace endpoint"
```

---

## 3. Pull Request (PR) Workflow

1. **Fork & Clone** the repository:
   ```bash
   git clone https://github.com/gargnikunj991-ux/AgriSathi_AI.git
   ```
2. **Create your feature branch**:
   ```bash
   git checkout -b feature/awesome-new-feature
   ```
3. **Make your changes** following our coding standards.
4. **Compile & Test**: Ensure clean compilation and test execution:
   ```bash
   cd agrisathi-backend
   mvn clean test
   ```
5. **Commit & Push**:
   ```bash
   git add .
   git commit -m "feat(module): add awesome new feature"
   git push origin feature/awesome-new-feature
   ```
6. **Open a Pull Request** against the `backend` or `main` branch with a description of your changes.

---

## 4. Java Coding Standards

- **Java Version**: Write clean Java 17 code using modern features (records, switch expressions, var where appropriate).
- **Lombok**: Use Lombok annotations (`@Data`, `@Getter`, `@Setter`, `@Builder`, `@RequiredArgsConstructor`) to reduce boilerplate code.
- **REST Envelope**: All controller responses MUST wrap payloads inside `ApiResponse<T>`.
- **Validation**: Validate incoming request body objects using `@Valid` and Jakarta constraints (`@NotBlank`, `@NotNull`, `@Size`).
- **Formatting**: Follow standard Java camelCase and PascalCase naming conventions.

---

## 5. Reporting Issues

When reporting bugs or requesting new features, please include:
- A clear, descriptive title.
- Steps to reproduce the issue.
- Expected vs actual behavior.
- Error logs or stack traces if applicable.
