package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {
    List<String> findAllDistinctType();
    List<Genre> findAllByType(String type);
}
