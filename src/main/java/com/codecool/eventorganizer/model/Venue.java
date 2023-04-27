package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    private String name;
    private URL homePage;
    private boolean isThereRefund;
    @Positive
    private int capacity;
    @OneToOne
    @Valid
    @NotNull
    private VenueAddress venueAddress;
    // TODO add insert only validation group
    private boolean inactive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venue venue)) return false;
        return isThereRefund() == venue.isThereRefund() && getCapacity() == venue.getCapacity()
                && isInactive() == venue.isInactive() && Objects.equals(getId(), venue.getId())
                && Objects.equals(getName(), venue.getName()) && Objects.equals(getHomePage(), venue.getHomePage())
                && Objects.equals(getVenueAddress(), venue.getVenueAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getHomePage(), isThereRefund(), getCapacity(), getVenueAddress(),
                isInactive());
    }
}
