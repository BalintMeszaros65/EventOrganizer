package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Performance getPerformance(UUID id) {
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
}
