# Architecture Overview: Agentic SDLC & URL Shortener Service

## 1. Executive Summary
This system embodies a dual architecture:
1. **Target Service:** A high-throughput, low-latency URL Shortener application built with Spring Boot, JPA/H2, Base62 encoding, and SSRF security guardrails.
2. **Orchestration Layer:** A state-machine SDLC agentic coordinator that enforces deterministic governance, non-linear recovery loops, human approval checkpoints, and audit trails.

## 2. Core Components
- **Base62 ID-based Encoding:** Avoids hash collisions by mapping auto-incrementing surrogate IDs to alphanumeric 62-base representations.
- **SSRF Defense Filter:** Validates target URLs before saving; blocks localhost, internal corporate domains, and cloud metadata IP addresses (`169.254.169.254`).
- **Telemetry & Lineage Engine:** Records every state transition, spec evolution, test cycle run, and security rule check.

## 3. Orchestration Control Flow
`Requirement Prompt` ➔ `RequirementsAgent (Normalization)` ➔ `ArchitectureAgent (DAG Tasks)` ➔ `Coder & QA Iterative Loop (Self-Healing)` ➔ `SecurityPolicyAgent (Static Rule Gates)` ➔ `HITL Gate (Human Oversight)` ➔ `Deployment`.