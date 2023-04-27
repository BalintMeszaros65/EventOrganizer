package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public abstract class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Valid
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
