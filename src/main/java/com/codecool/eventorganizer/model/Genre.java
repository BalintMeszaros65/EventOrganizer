package com.codecool.eventorganizer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
public class Genre {
    @Id
    // TODO ask if the annotation is not working due to UUID not being a CharSequence
    // @org.hibernate.validator.constraints.UUID
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "Name attribute must not be null, empty or blank.")
    private String name;
    @NotBlank(message = "Type attribute must not be null, empty or blank.")
    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre genre)) return false;
        return Objects.equals(id, genre.id) && Objects.equals(name, genre.name) && Objects.equals(type, genre.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }
}
