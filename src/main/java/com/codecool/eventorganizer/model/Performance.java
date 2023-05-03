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
    // TODO ask if the annotation is not working due to URL not being a String
//    @org.hibernate.validator.constraints.URL
    private URL homePage;
    @Valid
    @NotNull
    @ManyToOne
    private Genre genre;
    // TODO add insert only validation group
    private boolean inactive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Performance that)) return false;
        return isInactive() == that.isInactive() && Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName()) && Objects.equals(getHomePage(), that.getHomePage())
                && Objects.equals(getGenre(), that.getGenre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getHomePage(), getGenre(), isInactive());
    }
}
