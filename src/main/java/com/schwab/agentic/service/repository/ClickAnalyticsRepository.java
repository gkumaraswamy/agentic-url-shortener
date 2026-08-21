package com.schwab.agentic.service.repository;

import com.schwab.agentic.service.model.ClickAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClickAnalyticsRepository extends JpaRepository<ClickAnalytics, Long> {
    List<ClickAnalytics> findByShortCode(String shortCode);
    long countByShortCode(String shortCode);
}