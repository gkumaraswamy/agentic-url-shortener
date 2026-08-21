package com.schwab.agentic.service.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AnalyticsResponse {
    private String shortCode;
    private String originalUrl;
    private long totalClicks;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private List<ClickEntry> recentClicks;

    @Data
    @Builder
    public static class ClickEntry {
        private LocalDateTime timestamp;
        private String referer;
        private String userAgent;
    }
}