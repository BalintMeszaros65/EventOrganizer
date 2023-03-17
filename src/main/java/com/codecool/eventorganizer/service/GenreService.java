package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public List<String> getAllGenreTypes() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(Genre::getType)
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Genre> getAllGenreByType(String type) {
        return genreRepository.findAllByType(type);
    }

    // helper methods

    private static void checkIfRequiredDataExists(Genre genre) {
        if (genre.getName() == null || genre.getType() == null || "".equals(genre.getName())
                || "".equals(genre.getType())) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in genre.");
        }
    }

    private void checkIfGenreExists(UUID id) {
        if (!genreRepository.existsById(id)) {
            throw new NoSuchElementException("Genre not found by given id.");
        }
    }

    // logic

    public ResponseEntity<String> createGenre(Genre genre) {
        checkIfRequiredDataExists(genre);
        if (genre.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        genreRepository.save(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body("Genre successfully created.");
    }

    public ResponseEntity<String> updateGenre(Genre genre) {
        checkIfRequiredDataExists(genre);
        checkIfGenreExists(genre.getId());
        genreRepository.save(genre);
        return ResponseEntity.status(HttpStatus.OK).body("Genre successfully updated.");
    }

    public ResponseEntity<String> deleteGenre(UUID id) {
        checkIfGenreExists(id);
        genreRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Genre successfully deleted.");
    }
}
