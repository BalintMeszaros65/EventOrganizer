package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.util.UUID;

@Entity
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private String name;
    private URL homePage;
    @NotNull
    @ManyToOne
    private Genre genre;

    public Performance(String name, URL homePage, Genre genre) {
        this.name = name;
        this.homePage = homePage;
        this.genre = genre;
    }

    public Performance() {

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
