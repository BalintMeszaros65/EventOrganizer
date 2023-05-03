package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.utility.BasicInfoValidation;
import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import jakarta.persistence.*;
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
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Null(groups = CreateValidation.class, message = "Id must not exist when creating.")
    @NotNull(groups = {UpdateValidation.class, BasicInfoValidation.class})
    private UUID id;
    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class, BasicInfoValidation.class})
    // TODO ask how to catch SQL exception for user friendly error message
    @Column(unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return Objects.equals(getId(), country.getId()) && Objects.equals(getName(), country.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
