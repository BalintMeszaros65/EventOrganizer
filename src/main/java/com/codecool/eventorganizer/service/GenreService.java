package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public void saveAndUpdateGenre(Genre genre) {
        genreRepository.save(genre);
    }

    public List<String> getAllGenreTypes() {
        return genreRepository.findAllDistinctType();
    }

    public ResponseEntity<String> deleteGenre(Genre genre) {
        UUID id = genre.getId();
        if (!genreRepository.existsById(id)) {
            throw new NoSuchElementException("Genre not found by given id.");
        }
        genreRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Genre deleted successfully.");
    }

    public ResponseEntity<String> createGenre(Genre genre) {
        if (genre.getName() == null || genre.getType() == null || "".equals(genre.getName())
                || "".equals(genre.getType())) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in genre.");
        }
        saveAndUpdateGenre(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body("Genre created successfully.");
    }


    public ResponseEntity<String> updateGenre(Genre genre) {
        UUID id = genre.getId();
        if (id == null || genre.getName() == null || genre.getType() == null || "".equals(genre.getName())
                || "".equals(genre.getType())) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in genre.");
        }
        if (!genreRepository.existsById(id)) {
            throw new NoSuchElementException("Genre not found by given id.");
        }
        saveAndUpdateGenre(genre);
        return ResponseEntity.status(HttpStatus.OK).body("Genre updated successfully.");
    }
}
