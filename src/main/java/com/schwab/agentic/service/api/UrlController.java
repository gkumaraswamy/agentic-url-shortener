package com.schwab.agentic.service.api;

import com.schwab.agentic.service.dto.ShortenRequest;
import com.schwab.agentic.service.dto.ShortenResponse;
import com.schwab.agentic.service.service.AnalyticsService;
import com.schwab.agentic.service.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UrlController {

    private final UrlShortenerService shortenerService;
    private final AnalyticsService analyticsService;

    public UrlController(UrlShortenerService shortenerService, AnalyticsService analyticsService) {
        this.shortenerService = shortenerService;
        this.analyticsService = analyticsService;
    }

    @PostMapping("/api/v1/shorten")
    public ResponseEntity<ShortenResponse> createShortUrl(@Valid @RequestBody ShortenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shortenerService.shorten(request));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode,
            HttpServletRequest request,
            @RequestHeader(value = HttpHeaders.REFERER, required = false) String referer,
            @RequestHeader(value = HttpHeaders.USER_AGENT, required = false) String userAgent) {

        String originalUrl = shortenerService.resolveAndTrack(shortCode);
        analyticsService.recordClick(shortCode, referer, userAgent, request.getRemoteAddr());

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, originalUrl)
                .build();
    }
}