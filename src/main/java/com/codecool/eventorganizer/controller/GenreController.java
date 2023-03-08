package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenreController {
    GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping("/api/genre/create")
    public ResponseEntity<String> createGenre(@RequestBody Genre genre) {
        return genreService.createGenre(genre);
    }

    @PutMapping("/api/genre/update")
    public ResponseEntity<String> updateGenre(@RequestBody Genre genre) {
        return genreService.updateGenre(genre);
    }
}
