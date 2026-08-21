package com.schwab.agentic.orchestrator.agents;

import com.schwab.agentic.orchestrator.engine.SdlcState;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityPolicyAgent implements Agent {
    @Override
    public String getName() { return "Security & Policy Guardrail Agent"; }

    @Override
    public SdlcState execute(SdlcState state) {
        state.addTrace("SecurityPolicyAgent: Validating code against organizational security policies...");
        List<String> violations = new ArrayList<>();

        // Policy 1: Must prevent SSRF
        if (state.getGeneratedArtifacts() != null && 
            state.getGeneratedArtifacts().values().stream().noneMatch(c -> c.contains("SSRF") || c.contains("validateUrl"))) {
            violations.add("POLICY_VIOLATION_001: Missing SSRF filtering on target redirects.");
        }

        if (violations.isEmpty()) {
            state.setSecurityApproved(true);
            state.addTrace("SecurityPolicyAgent: All compliance and security policies PASSED.");
        } else {
            state.setSecurityApproved(false);
            state.setSecurityViolations(violations);
            state.addTrace("SecurityPolicyAgent: Security Gate FAILED with " + violations.size() + " violations.");
        }
        return state;
    }
}