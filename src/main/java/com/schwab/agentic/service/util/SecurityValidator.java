package com.schwab.agentic.service.util;

import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.List;

@Component
public class SecurityValidator {
    private static final List<String> BLOCKED_HOSTS = List.of(
        "localhost", "127.0.0.1", "0.0.0.0", "169.254.169.254", "internal.corp"
    );

    public void validateUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Target URL cannot be empty");
        }
        try {
            URI parsed = URI.create(url);
            String scheme = parsed.getScheme();
            if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
                throw new IllegalArgumentException("Invalid scheme: Only HTTP/HTTPS protocols are permitted.");
            }
            String host = parsed.getHost();
            if (host == null || BLOCKED_HOSTS.contains(host.toLowerCase())) {
                throw new SecurityException("SSRF Guardrail Violation: Redirection to internal host is strictly blocked.");
            }
        } catch (IllegalArgumentException | SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Malformed URL target: " + e.getMessage());
        }
    }
}