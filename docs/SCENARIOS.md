# Evaluation Scenarios

### Scenario 1: Greenfield (New System)
- **Input:** "Build a URL shortener with Base62 encoding, SQLite/H2 storage, analytics, and standard redirect endpoints."
- **Decomposition:** Normalized into 5 sub-tasks across entity modeling, encoding, controller endpoints, and test fixtures.
- **Result:** $100\%$ test coverage, full audit trail recorded.

### Scenario 2: Brownfield (Enhancement & Refactoring)
- **Input:** "Add a Time-To-Live (TTL) expiration feature and an active click-tracking analytics endpoint."
- **Decomposition:** AST-based code reasoning, schema update (`expiresAt`), and non-breaking REST endpoint extension.

### Scenario 3: Ambiguous Requirements
- **Input:** "Make the shortener secure and enterprise-grade."
- **Decomposition:** Ambiguity detection flags vague requirements and normalizes them into explicit policy controls (SSRF protection, validation filters). Halts at HITL gate for confirmation.