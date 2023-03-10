package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/api/performance/update")
    public ResponseEntity<String> updatePerformance(@RequestBody Performance performance) {
        return performanceService.updatePerformance(performance);
    }

    @DeleteMapping("/api/performance/delete")
    public ResponseEntity<String> deletePerformance(@RequestBody Performance performance) {
        return performanceService.deletePerformance(performance);
    }
}
