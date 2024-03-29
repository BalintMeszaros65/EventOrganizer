package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // basic CRUD operations

    public Genre getGenreById(UUID id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        if (optionalGenre.isPresent()) {
            return optionalGenre.get();
        } else {
            throw new NoSuchElementException("Genre not found by given id.");
        }
    }

    public Set<String> getAllGenreTypes() {
        return genreRepository.findAll()
                .stream()
                .map(Genre::getType)
                .collect(Collectors.toSet());
    }

    public Set<Genre> getAllGenreByType(String type) {
        return genreRepository.findAllDistinctByType(type);
    }

    // helper methods

    private void checkIfGenreExists(UUID id) {
        if (!genreRepository.existsById(id)) {
            throw new NoSuchElementException("Genre not found by given id.");
        }
    }

    // logic

    public ResponseEntity<String> createGenre(Genre genre) {
        genreRepository.save(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body("Genre successfully created.");
    }

    public ResponseEntity<String> updateGenre(Genre genre) {
        checkIfGenreExists(genre.getId());
        genreRepository.save(genre);
        return ResponseEntity.status(HttpStatus.OK).body("Genre successfully updated.");
    }

    public ResponseEntity<String> activateGenre(UUID id) {
        Genre genre = getGenreById(id);
        genre.setInactive(false);
        genreRepository.save(genre);
        return ResponseEntity.status(HttpStatus.OK).body("Genre successfully activated.");
    }

    public ResponseEntity<String> inactivateGenre(UUID id) {
        Genre genre = getGenreById(id);
        genre.setInactive(true);
        genreRepository.save(genre);
        return ResponseEntity.status(HttpStatus.OK).body("Genre successfully inactivated.");
    }
}
