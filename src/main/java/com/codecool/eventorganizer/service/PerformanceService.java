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

    // basic CRUD operations

    public Performance getPerformanceById(UUID id) {
        Optional<Performance> optionalPerformance = performanceRepository.findById(id);
        if (optionalPerformance.isPresent()) {
            return optionalPerformance.get();
        } else {
            throw new NoSuchElementException("Performance not found by given id.");
        }
    }

    // helper methods

    private void checkIfPerformanceExists(UUID id) {
        if (!performanceRepository.existsById(id)) {
            throw new NoSuchElementException("Performance not found by given id.");
        }
    }

    private static void checkIfRequiredDataExists(Performance performance) {
        String name = performance.getName();
        if (performance.getGenre() == null || name == null || "".equals(name)) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in performance.");
        }
    }

    // logic

    public ResponseEntity<String> createPerformance(Performance performance) {
        checkIfRequiredDataExists(performance);
        if (performance.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        performanceRepository.save(performance);
        return ResponseEntity.status(HttpStatus.CREATED).body("Performance successfully created.");
    }

    public ResponseEntity<String> updatePerformance(Performance performance) {
        checkIfRequiredDataExists(performance);
        checkIfPerformanceExists(performance.getId());
        performanceRepository.save(performance);
        return ResponseEntity.status(HttpStatus.OK).body("Performance successfully updated.");
    }

    // TODO implement logic to handle booked events (already done and upcoming)
    public ResponseEntity<String> deletePerformance(Performance performance) {
        UUID id = performance.getId();
        checkIfPerformanceExists(id);
        performanceRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Performance successfully deleted.");
    }
}
