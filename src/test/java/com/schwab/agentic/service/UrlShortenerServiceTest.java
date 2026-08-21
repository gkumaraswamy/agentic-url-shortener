package com.schwab.agentic.service;

import com.schwab.agentic.service.dto.ShortenRequest;
import com.schwab.agentic.service.dto.ShortenResponse;
import com.schwab.agentic.service.service.UrlShortenerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UrlShortenerServiceTest {

    @Autowired
    private UrlShortenerService shortenerService;

    @Test
    @DisplayName("Greenfield: Should shorten valid URL and resolve successfully")
    void testShortenAndResolve() {
        ShortenRequest request = new ShortenRequest();
        request.setOriginalUrl("https://www.schwab.com/investment-products");
        
        ShortenResponse response = shortenerService.shorten(request);
        
        assertNotNull(response.getShortCode());
        assertEquals("https://www.schwab.com/investment-products", response.getOriginalUrl());

        String resolved = shortenerService.resolveAndTrack(response.getShortCode());
        assertEquals(request.getOriginalUrl(), resolved);
    }

    @Test
    @DisplayName("Security Gate: Should throw SecurityException on internal SSRF attempt")
    void testSsrfProtection() {
        ShortenRequest request = new ShortenRequest();
        request.setOriginalUrl("http://169.254.169.254/latest/meta-data/");

        assertThrows(SecurityException.class, () -> shortenerService.shorten(request));
    }
}