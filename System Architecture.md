agentic-url-shortener/
├── pom.xml
├── README.md
├── docs/
│   ├── ARCHITECTURE.md
│   ├── SCENARIOS.md
│   └── INTERVIEW_TALKING_POINTS.md
└── src/
    ├── main/
    │   ├── java/com/schwab/agentic/
    │   │   ├── Application.java
    │   │   ├── orchestrator/
    │   │   │   ├── engine/
    │   │   │   │   ├── SdlcState.java
    │   │   │   │   ├── ExecutionGraph.java
    │   │   │   │   └── OrchestrationEngine.java
    │   │   │   ├── agents/
    │   │   │   │   ├── Agent.java
    │   │   │   │   ├── RequirementsAgent.java
    │   │   │   │   ├── ArchitectureAgent.java
    │   │   │   │   ├── CodingAgent.java
    │   │   │   │   ├── TestingAgent.java
    │   │   │   │   ├── SecurityPolicyAgent.java
    │   │   │   │   └── ReleaseAgent.java
    │   │   │   ├── governance/
    │   │   │   │   ├── HitlGate.java
    │   │   │   │   └── PolicyRule.java
    │   │   │   └── telemetry/
    │   │   │       └── SdlcTelemetryLogger.java
    │   │   └── service/
    │   │       ├── api/
    │   │       │   ├── UrlController.java
    │   │       │   └── AnalyticsController.java
    │   │       ├── dto/
    │   │       │   ├── ShortenRequest.java
    │   │       │   ├── ShortenResponse.java
    │   │       │   └── AnalyticsResponse.java
    │   │       ├── model/
    │   │       │   ├── UrlMapping.java
    │   │       │   └── ClickAnalytics.java
    │   │       ├── repository/
    │   │       │   ├── UrlMappingRepository.java
    │   │       │   └── ClickAnalyticsRepository.java
    │   │       ├── util/
    │   │       │   ├── Base62Encoder.java
    │   │       │   └── SecurityValidator.java
    │   │       └── service/
    │   │           ├── UrlShortenerService.java
    │   │           └── AnalyticsService.java
    │   └── resources/
    │       └── application.yml
    └── test/
        └── java/com/schwab/agentic/
            ├── service/UrlShortenerServiceTest.java
            └── orchestrator/OrchestrationEngineTest.java