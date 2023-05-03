package com.codecool.eventorganizer.model;

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
public abstract class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Null(groups = CreateValidation.class, message = "Id must not exist when creating.")
    @NotNull(groups = UpdateValidation.class)
    private UUID id;
    @NotNull
    @ManyToOne
    private City city;
    @NotBlank
    private String zipCode;
    @NotBlank
    private String street;
    @NotBlank
    private String house;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return Objects.equals(getId(), address.getId()) && Objects.equals(getCity(), address.getCity())
                && Objects.equals(getZipCode(), address.getZipCode()) && Objects.equals(getStreet(), address.getStreet())
                && Objects.equals(getHouse(), address.getHouse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCity(), getZipCode(), getStreet(), getHouse());
    }
}
