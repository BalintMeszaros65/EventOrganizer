package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    private String name;
    @org.hibernate.validator.constraints.URL
    private URL homePage;
    @Valid
    @NotNull
    @ManyToOne
    private Genre genre;
    private boolean inactive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Performance performance)) return false;
        return inactive == performance.inactive && Objects.equals(id, performance.id)
                && Objects.equals(name, performance.name) && Objects.equals(homePage, performance.homePage)
                && Objects.equals(genre, performance.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, homePage, genre, inactive);
    }
}
