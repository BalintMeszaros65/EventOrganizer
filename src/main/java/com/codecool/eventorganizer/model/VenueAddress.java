package com.codecool.eventorganizer.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class VenueAddress extends Address {

    private String googleMapsReference;

    public VenueAddress(UUID id, City city, String zipCode, String street, String house, String googleMapsReference) {
        super(id, city, zipCode, street, house);
        this.googleMapsReference = googleMapsReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VenueAddress that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getGoogleMapsReference(), that.getGoogleMapsReference());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGoogleMapsReference());
    }
}
