package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull
    @Column(unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return Objects.equals(id, country.id) && Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
