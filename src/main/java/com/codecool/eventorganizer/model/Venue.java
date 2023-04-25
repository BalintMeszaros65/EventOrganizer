package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @Min(1)
    private int capacity;
    @OneToOne
    @Valid
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
