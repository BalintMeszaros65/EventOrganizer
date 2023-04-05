package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.service.PerformanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Basic Authentication")
@Tag(
        name = "Performance",
        description = "Operations about performance"
)
public class PerformanceController {
    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/api/performance/create")
    public ResponseEntity<String> createPerformance(@RequestBody Performance performance) {
        return performanceService.createPerformance(performance);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/performance/update")
    public ResponseEntity<String> updatePerformance(@RequestBody Performance performance) {
        return performanceService.updatePerformance(performance);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/performance/delete/{id}")
    public ResponseEntity<String> deletePerformance(@PathVariable UUID id) {
        return performanceService.deletePerformance(id);
    }
}
