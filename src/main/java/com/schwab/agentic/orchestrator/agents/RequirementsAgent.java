package com.schwab.agentic.orchestrator.agents;

import com.schwab.agentic.orchestrator.engine.SdlcState;
import org.springframework.stereotype.Component;

@Component
public class RequirementsAgent implements Agent {
    @Override
    public String getName() { return "Requirements Analysis Agent"; }

    @Override
    public SdlcState execute(SdlcState state) {
        state.addTrace("RequirementsAgent: Parsing requirement -> " + state.getRawRequirement());
        
        // Ambiguity resolution logic
        if (state.getRawRequirement().toLowerCase().contains("secure") || 
            state.getRawRequirement().toLowerCase().contains("enterprise")) {
            state.setNormalizedSpec("SPEC-ENTERPRISE: Implement SSRF filter, rate limit placeholders, Base62 encoding, and audit telemetry.");
        } else if (state.getRawRequirement().toLowerCase().contains("ttl")) {
            state.setNormalizedSpec("SPEC-BROWNFIELD: Add TTL expiry column, migration support, and update redirect resolution logic.");
        } else {
            state.setNormalizedSpec("SPEC-GREENFIELD: Full CRUD URL shortener with Base62 ID encoding and in-memory click analytics.");
        }
        
        state.addTrace("RequirementsAgent: Spec normalized -> " + state.getNormalizedSpec());
        return state;
    }
}