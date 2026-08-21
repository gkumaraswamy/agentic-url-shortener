package com.schwab.agentic.orchestrator.agents;

import com.schwab.agentic.orchestrator.engine.SdlcState;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ArchitectureAgent implements Agent {
    @Override
    public String getName() { return "Architecture & Decomposition Agent"; }

    @Override
    public SdlcState execute(SdlcState state) {
        state.addTrace("ArchitectureAgent: Decomposing spec into actionable DAG nodes.");
        state.setTaskGraph(List.of(
            "TASK-1: Data Schema Definition (UrlMapping, ClickAnalytics)",
            "TASK-2: Base62 & SSRF Core Security Utilities",
            "TASK-3: Service Layer & Transactional Logic",
            "TASK-4: REST Controllers & OpenAPI Contract",
            "TASK-5: Unit & Integration Test Suites"
        ));
        return state;
    }
}