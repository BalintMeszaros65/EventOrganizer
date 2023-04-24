package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
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
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private String name;
    private URL homePage;
    @NotNull
    private boolean isThereRefund;
    @NotNull
    private int capacity;
    @OneToOne
    @NotNull
    private VenueAddress venueAddress;
    private boolean inactive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venue venue)) return false;
        return isThereRefund == venue.isThereRefund && capacity == venue.capacity && inactive == venue.inactive
                && Objects.equals(id, venue.id) && Objects.equals(name, venue.name)
                && Objects.equals(homePage, venue.homePage) && Objects.equals(venueAddress, venue.venueAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, homePage, isThereRefund, capacity, venueAddress, inactive);
    }
}
