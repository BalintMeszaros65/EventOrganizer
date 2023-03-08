package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PerformanceController {
    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping("/api/performance/create")
    public ResponseEntity<String> createPerformance(@RequestBody Performance performance) {
        return performanceService.createPerformance(performance);
    }
}
