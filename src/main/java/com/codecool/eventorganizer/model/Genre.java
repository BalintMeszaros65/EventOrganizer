package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
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
public class Genre implements RecommendationWeightable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Null(groups = CreateValidation.class, message = "Id must not exist when creating.")
    @NotNull(groups = UpdateValidation.class)
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    @AssertFalse(groups = {CreateValidation.class, UpdateValidation.class},
            message = "Genre can not be inactive when creating/updating.")
    private boolean inactive;

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

    @Override
    public int calculateWeight() {
        return 13;
    }
}
