package com.schwab.agentic.orchestrator;

import com.schwab.agentic.orchestrator.engine.OrchestrationEngine;
import com.schwab.agentic.orchestrator.engine.SdlcState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrchestrationEngineTest {

    @Autowired
    private OrchestrationEngine orchestrationEngine;

    @Test
    @DisplayName("Orchestrator: Should execute Greenfield scenario with HITL gate pass")
    void testGreenfieldExecution() {
        SdlcState state = orchestrationEngine.executeScenario(
            "Greenfield Build", 
            "Create URL shortener with Base62 encoding", 
            true
        );

        assertTrue(state.isTestsPassed());
        assertTrue(state.isSecurityApproved());
        assertTrue(state.isHitlApproved());
        assertEquals("DEPLOYED_TO_STAGE", state.getDeploymentStatus());
        assertFalse(state.getTraceAuditLogs().isEmpty());
    }

    @Test
    @DisplayName("Orchestrator: Ambiguous scenario triggers normalization and human gate control")
    void testAmbiguousScenarioExecution() {
        SdlcState state = orchestrationEngine.executeScenario(
            "Ambiguous Requirement", 
            "Make service enterprise ready and secure", 
            false
        );

        assertTrue(state.getNormalizedSpec().startsWith("SPEC-ENTERPRISE"));
        assertEquals("BLOCKED_BY_HUMAN_OVERSIGHT", state.getDeploymentStatus());
    }
}