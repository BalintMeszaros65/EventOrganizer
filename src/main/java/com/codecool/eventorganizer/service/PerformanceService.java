package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PerformanceService {
    private final PerformanceRepository performanceRepository;

    @Autowired
    public PerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    public void saveAndUpdatePerformance(Performance performance) {
        performanceRepository.save(performance);
    }

    public Performance getPerformanceById(UUID id) {
        Optional<Performance> optionalPerformance = performanceRepository.findById(id);
        if (optionalPerformance.isPresent()) {
            return optionalPerformance.get();
        } else {
            throw new NoSuchElementException("Performance not found by given id.");
        }
    }

    public void deletePerformance(UUID id) {
        performanceRepository.deleteById(id);
    }

    public ResponseEntity<String> createPerformance(Performance performance) {
        String name = performance.getName();
        if (performance.getGenre() == null || name == null || "".equals(name)) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in performance.");
        }
        saveAndUpdatePerformance(performance);
        return ResponseEntity.status(HttpStatus.CREATED).body("Performance successfully created.");
    }
}
