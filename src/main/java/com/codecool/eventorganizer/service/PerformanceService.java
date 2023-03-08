package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.Genre;
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
    private final GenreService genreService;

    @Autowired
    public PerformanceService(PerformanceRepository performanceRepository, GenreService genreService) {
        this.performanceRepository = performanceRepository;
        this.genreService = genreService;
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

    public ResponseEntity<String> createPerformance(Performance performance, UUID genreId) {
        Genre genre = genreService.getGenreById(genreId);
        String name = performance.getName();
        if (name == null || "".equals(name)) {
            throw new CustomExceptions.MissingAttributeException("Name is missing.");
        }
        performance.setGenre(genre);
        saveAndUpdatePerformance(performance);
        return ResponseEntity.status(HttpStatus.CREATED).body("Performance successfully created.");
    }
}
