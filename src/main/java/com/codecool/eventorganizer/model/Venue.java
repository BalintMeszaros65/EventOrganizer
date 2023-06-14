package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
public class Venue implements RecommendationWeightable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Null(groups = CreateValidation.class, message = "Id must not exist when creating.")
    @NotNull(groups = UpdateValidation.class)
    private UUID id;
    @NotBlank
    private String name;
    private URL homePage;
    private boolean isThereRefund;
    @Positive
    private int capacity;
    @OneToOne
    @NotNull
    private VenueAddress venueAddress;
    @AssertFalse(groups = {CreateValidation.class, UpdateValidation.class},
            message = "Venue can not be inactive when creating/updating.")
    private boolean inactive;


    public City getCity() {
        return venueAddress.getCity();
    }

    public Country getCountry() {
        return venueAddress.getCountry();
    }

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

    @Override
    public int calculateWeight() {
        return 12;
    }
}
