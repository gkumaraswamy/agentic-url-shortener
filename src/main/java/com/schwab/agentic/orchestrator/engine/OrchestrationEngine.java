package com.schwab.agentic.orchestrator.engine;

import com.schwab.agentic.orchestrator.agents.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrchestrationEngine {

    private final RequirementsAgent requirementsAgent;
    private final ArchitectureAgent architectureAgent;
    private final SecurityPolicyAgent securityPolicyAgent;

    public OrchestrationEngine(
            RequirementsAgent requirementsAgent,
            ArchitectureAgent architectureAgent,
            SecurityPolicyAgent securityPolicyAgent) {
        this.requirementsAgent = requirementsAgent;
        this.architectureAgent = architectureAgent;
        this.securityPolicyAgent = securityPolicyAgent;
    }

    public SdlcState executeScenario(String scenarioName, String requirement, boolean grantHitlApproval) {
        long startTime = System.currentTimeMillis();
        
        SdlcState state = SdlcState.builder()
                .executionId(UUID.randomUUID().toString().substring(0, 8))
                .scenarioName(scenarioName)
                .rawRequirement(requirement)
                .traceAuditLogs(new ArrayList<>())
                .generatedArtifacts(new HashMap<>())
                .retryCount(0)
                .build();

        state.addTrace("ORCHESTRATOR: Starting run for scenario [" + scenarioName + "]");

        // Step 1: Requirements Normalization
        state = requirementsAgent.execute(state);

        // Step 2: Architecture & Decomposition
        state = architectureAgent.execute(state);

        // Step 3: Simulation of Code Generation & Self-Healing Execution Loop
        int attempts = 0;
        final int MAX_RETRIES = 3;
        boolean passedTesting = false;

        while (attempts < MAX_RETRIES && !passedTesting) {
            attempts++;
            state.addTrace("CODER_AGENT: Generation attempt #" + attempts);
            
            // Mock source generation
            state.getGeneratedArtifacts().put("UrlShortenerService.java", "class UrlShortenerService { void validateUrl() { ... } }");
            
            // QA / Test Execution simulation
            state.addTrace("TEST_RUNNER: Executing unit and integration suites...");
            passedTesting = true; // Simulating successful run
            state.setTestsPassed(true);
        }

        state.setRetryCount(attempts - 1);

        // Step 4: Security Policy Gate
        state = securityPolicyAgent.execute(state);

        // Step 5: Human-In-The-Loop (HITL) Gate
        if (state.isSecurityApproved() && state.isTestsPassed()) {
            if (grantHitlApproval) {
                state.setHitlApproved(true);
                state.setDeploymentStatus("DEPLOYED_TO_STAGE");
                state.addTrace("HITL_GATE: Human approved release change.");
            } else {
                state.setHitlApproved(false);
                state.setDeploymentStatus("BLOCKED_BY_HUMAN_OVERSIGHT");
                state.addTrace("HITL_GATE: Human denied release change.");
            }
        } else {
            state.setDeploymentStatus("FAILED_POLICY_GATE");
        }

        state.setExecutionLatencyMs(System.currentTimeMillis() - startTime);
        state.addTrace("ORCHESTRATOR: Execution finished with status: " + state.getDeploymentStatus());
        return state;
    }
}