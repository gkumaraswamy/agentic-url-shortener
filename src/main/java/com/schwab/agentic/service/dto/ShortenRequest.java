package com.schwab.agentic.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShortenRequest {
    @NotBlank(message = "Original URL is required")
    private String originalUrl;
    
    private String customAlias;
    private Integer ttlDays;
}