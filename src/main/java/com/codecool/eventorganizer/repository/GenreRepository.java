package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
}
