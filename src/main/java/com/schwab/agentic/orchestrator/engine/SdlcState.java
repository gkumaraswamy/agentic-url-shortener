package com.schwab.agentic.orchestrator.engine;

import lombok.Builder;
import lombok.Data;
import java.util.*;

@Data
@Builder
public class SdlcState {
    private String executionId;
    private String scenarioName;
    private String rawRequirement;
    
    // Lineage & Stage Outputs
    private String normalizedSpec;
    private List<String> taskGraph;
    private Map<String, String> generatedArtifacts;
    private boolean testsPassed;
    private List<String> testLogs;
    private boolean securityApproved;
    private List<String> securityViolations;
    private boolean hitlApproved;
    private String deploymentStatus;

    // Metrics
    private int retryCount;
    private long executionLatencyMs;
    private List<String> traceAuditLogs;

    public void addTrace(String message) {
        if (traceAuditLogs == null) traceAuditLogs = new ArrayList<>();
        traceAuditLogs.add(new Date() + " | " + message);
    }
}