package com.schwab.agentic.service.service;

import com.schwab.agentic.service.dto.ShortenRequest;
import com.schwab.agentic.service.dto.ShortenResponse;
import com.schwab.agentic.service.model.UrlMapping;
import com.schwab.agentic.service.repository.UrlMappingRepository;
import com.schwab.agentic.service.util.Base62Encoder;
import com.schwab.agentic.service.util.SecurityValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UrlShortenerService {

    private final UrlMappingRepository repository;
    private final Base62Encoder base62Encoder;
    private final SecurityValidator securityValidator;
    private final String baseUrl;
    private final int defaultTtlDays;

    public UrlShortenerService(
            UrlMappingRepository repository,
            Base62Encoder base62Encoder,
            SecurityValidator securityValidator,
            @Value("${app.base-url:http://localhost:8080/}") String baseUrl,
            @Value("${app.default-ttl-days:30}") int defaultTtlDays) {
        this.repository = repository;
        this.base62Encoder = base62Encoder;
        this.securityValidator = securityValidator;
        this.baseUrl = baseUrl;
        this.defaultTtlDays = defaultTtlDays;
    }

    @Transactional
    public ShortenResponse shorten(ShortenRequest request) {
        securityValidator.validateUrl(request.getOriginalUrl());

        String shortCode;
        if (request.getCustomAlias() != null && !request.getCustomAlias().isBlank()) {
            shortCode = request.getCustomAlias().trim();
            if (repository.existsByShortCode(shortCode)) {
                throw new IllegalArgumentException("Custom alias '" + shortCode + "' is already in use.");
            }
        } else {
            // Reserve entity to generate sequence ID, then Base62 encode
            UrlMapping temp = repository.save(UrlMapping.builder()
                    .longUrl(request.getOriginalUrl())
                    .shortCode("T" + Long.toString(System.nanoTime(), 36))
                    .createdAt(LocalDateTime.now())
                    .clickCount(0L)
                    .build());
            shortCode = base62Encoder.encode(temp.getId() + 1000000L); // Offset to guarantee minimum length
            repository.delete(temp);
        }

        int ttl = (request.getTtlDays() != null && request.getTtlDays() > 0) ? request.getTtlDays() : defaultTtlDays;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(ttl);

        UrlMapping mapping = UrlMapping.builder()
                .longUrl(request.getOriginalUrl())
                .shortCode(shortCode)
                .createdAt(now)
                .expiresAt(expiresAt)
                .clickCount(0L)
                .build();

        repository.save(mapping);

        return ShortenResponse.builder()
                .shortCode(shortCode)
                .shortUrl(baseUrl + shortCode)
                .originalUrl(mapping.getLongUrl())
                .createdAt(now)
                .expiresAt(expiresAt)
                .build();
    }

    @Transactional
    public String resolveAndTrack(String shortCode) {
        UrlMapping mapping = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new IllegalArgumentException("Short code not found: " + shortCode));

        if (mapping.getExpiresAt() != null && mapping.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Link has expired.");
        }

        mapping.setClickCount(mapping.getClickCount() + 1);
        repository.save(mapping);

        return mapping.getLongUrl();
    }
}