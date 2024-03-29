package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.service.GenreService;
import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Basic Authentication")
@Tag(
        name = "Genre",
        description = "Operations about genre"
)
@Validated
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/api/genre/create")
    public ResponseEntity<String> createGenre(@Validated(CreateValidation.class) @RequestBody Genre genre) {
        return genreService.createGenre(genre);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/genre/update")
    public ResponseEntity<String> updateGenre(@Validated(UpdateValidation.class) @RequestBody Genre genre) {
        return genreService.updateGenre(genre);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/genre/switch-active-state/{id}")
    public ResponseEntity<String> activateGenre(@NotNull @PathVariable UUID id) {
        return genreService.activateGenre(id);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/genre/switch-active-state/{id}")
    public ResponseEntity<String> inactivateGenre(@NotNull @PathVariable UUID id) {
        return genreService.inactivateGenre(id);
    }

    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @GetMapping("/api/genre/types")
    public Set<String> getGenreTypes() {
        return genreService.getAllGenreTypes();
    }

    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @GetMapping("/api/genre/get-all-by-type/{type}")
    public Set<Genre> getAllGenreByType(@NotBlank @PathVariable String type) {
        return genreService.getAllGenreByType(type);
    }
}
