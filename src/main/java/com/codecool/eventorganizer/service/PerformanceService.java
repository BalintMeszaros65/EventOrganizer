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

    private void checkIfRequiredDataExists(Performance performance) {
        Genre genre = performance.getGenre();
        if (genre.isInactive()) {
            throw new IllegalArgumentException("Genre can not be inactive when creating/updating Performance.");
        }
        Genre savedGenre = genreService.getGenreById(genre.getId());
        if (!genre.equals(savedGenre)) {
            throw new IllegalArgumentException("Genre's data does not match with the one in database.");
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

    public ResponseEntity<String> switchActiveStateOfPerformance(UUID id) {
        Performance performance = getPerformanceById(id);
        boolean inactive = performance.isInactive();
        performance.setInactive(!inactive);
        performanceRepository.save(performance);
        return ResponseEntity.status(HttpStatus.OK).body(inactive ? "Performance successfully activated."
                : "Performance successfully inactivated.");
    }
}
