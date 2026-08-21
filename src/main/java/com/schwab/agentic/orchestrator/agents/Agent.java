package com.schwab.agentic.orchestrator.agents;

import com.schwab.agentic.orchestrator.engine.SdlcState;

public interface Agent {
    String getName();
    SdlcState execute(SdlcState state);
}