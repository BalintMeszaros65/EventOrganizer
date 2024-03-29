package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
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
public class Performance implements RecommendationWeightable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Null(groups = CreateValidation.class, message = "Id must not exist when creating.")
    @NotNull(groups = UpdateValidation.class)
    private UUID id;
    @NotBlank
    private String name;
    private URL homePage;
    @NotNull
    @ManyToOne
    private Genre genre;
    @AssertFalse(groups = {CreateValidation.class, UpdateValidation.class},
            message = "Performance can not be inactive when creating/updating.")
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

    @Override
    public int calculateWeight() {
        return 15;
    }
}
