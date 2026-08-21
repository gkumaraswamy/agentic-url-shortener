package com.schwab.agentic.service.service;

import com.schwab.agentic.service.dto.AnalyticsResponse;
import com.schwab.agentic.service.model.ClickAnalytics;
import com.schwab.agentic.service.model.UrlMapping;
import com.schwab.agentic.service.repository.ClickAnalyticsRepository;
import com.schwab.agentic.service.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnalyticsService {
    private final UrlMappingRepository mappingRepository;
    private final ClickAnalyticsRepository analyticsRepository;

    public AnalyticsService(UrlMappingRepository mappingRepository, ClickAnalyticsRepository analyticsRepository) {
        this.mappingRepository = mappingRepository;
        this.analyticsRepository = analyticsRepository;
    }

    public void recordClick(String shortCode, String referer, String userAgent, String ip) {
        analyticsRepository.save(ClickAnalytics.builder()
                .shortCode(shortCode)
                .clickedAt(LocalDateTime.now())
                .referer(referer != null ? referer : "Direct")
                .userAgent(userAgent != null ? userAgent : "Unknown")
                .ipAddress(ip != null ? ip : "0.0.0.0")
                .build());
    }

    public AnalyticsResponse getAnalytics(String shortCode) {
        UrlMapping mapping = mappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new IllegalArgumentException("Short code not found: " + shortCode));

        List<ClickAnalytics> records = analyticsRepository.findByShortCode(shortCode);

        List<AnalyticsResponse.ClickEntry> recent = records.stream()
                .map(r -> AnalyticsResponse.ClickEntry.builder()
                        .timestamp(r.getClickedAt())
                        .referer(r.getReferer())
                        .userAgent(r.getUserAgent())
                        .build())
                .toList();

        return AnalyticsResponse.builder()
                .shortCode(shortCode)
                .originalUrl(mapping.getLongUrl())
                .totalClicks(mapping.getClickCount())
                .createdAt(mapping.getCreatedAt())
                .expiresAt(mapping.getExpiresAt())
                .recentClicks(recent)
                .build();
    }
}