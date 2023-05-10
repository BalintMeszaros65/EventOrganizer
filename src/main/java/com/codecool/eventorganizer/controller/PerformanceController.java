package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.service.PerformanceService;
import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<String> createPerformance(@Validated(CreateValidation.class) @RequestBody Performance performance) {
        return performanceService.createPerformance(performance);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/performance/update")
    public ResponseEntity<String> updatePerformance(@Validated(UpdateValidation.class) @RequestBody Performance performance) {
        return performanceService.updatePerformance(performance);
    }

    @Secured("ROLE_ADMIN")
    // TODO ask if method is ok
    // split it to 2 endpoints PUT, DELETE
    @DeleteMapping("/api/performance/switch-active-state/{id}")
    public ResponseEntity<String> switchActiveStateOfPerformance(@NotNull @PathVariable UUID id) {
        return performanceService.switchActiveStateOfPerformance(id);
    }
}
