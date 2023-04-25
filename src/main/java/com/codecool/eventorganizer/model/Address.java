package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
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
public abstract class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Valid
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
        return Objects.equals(id, address.id) && Objects.equals(city, address.city)
                && Objects.equals(zipCode, address.zipCode)
                && Objects.equals(street, address.street) && Objects.equals(house, address.house);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, zipCode, street, house);
    }
}
