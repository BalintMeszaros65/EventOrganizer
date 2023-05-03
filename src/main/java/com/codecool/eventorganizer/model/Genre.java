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
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    // TODO add insert only validation group
    boolean inactive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre genre)) return false;
        return isInactive() == genre.isInactive() && Objects.equals(getId(), genre.getId()) && Objects.equals(getName(),
                genre.getName()) && Objects.equals(getType(), genre.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getType(), isInactive());
    }
}
