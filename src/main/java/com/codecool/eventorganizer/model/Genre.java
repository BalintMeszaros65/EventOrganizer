package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.utility.CreationInfoValidation;
import com.codecool.eventorganizer.utility.UpdateInfoValidation;
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
public class Genre {
    @Id
    // TODO ask if the annotation is not working due to UUID not being a CharSequence
    // @org.hibernate.validator.constraints.UUID
    @GeneratedValue(strategy = GenerationType.UUID)
    @Null(groups = CreationInfoValidation.class, message = "Id must not exist when creating.")
    @NotNull(groups = UpdateInfoValidation.class)
    private UUID id;
    @NotBlank(groups = {CreationInfoValidation.class, UpdateInfoValidation.class})
    private String name;
    @NotBlank(groups = {CreationInfoValidation.class, UpdateInfoValidation.class})
    private String type;
    @AssertFalse(groups = {CreationInfoValidation.class, UpdateInfoValidation.class},
            message = "Genre must be active when creating/updating.")
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
}
