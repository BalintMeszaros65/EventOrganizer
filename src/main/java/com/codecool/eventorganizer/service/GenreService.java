package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
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

    public Genre getGenre(UUID id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        if (optionalGenre.isPresent()) {
            return optionalGenre.get();
        } else {
            throw new NoSuchElementException("Genre not found by given id.");
        }
    }

    public void deleteGenre(UUID id) {
        genreRepository.deleteById(id);
    }
}
