package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GenreController {
    private final GenreService genreService;

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

    @DeleteMapping("/api/genre/delete")
    public ResponseEntity<String> deleteGenre(@RequestBody Genre genre) {
        return genreService.deleteGenre(genre);
    }

    @GetMapping("/api/genre/types")
    public List<String> getGenreTypes() {
        return genreService.getAllGenreTypes();
    }

    @GetMapping("/api/genre/get-all-by-type/{type}")
    public List<Genre> getAllGenreByType(@PathVariable String type) {
        return genreService.getAllGenreByType(type);
    }
}
