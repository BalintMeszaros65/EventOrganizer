package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.net.URL;
import java.util.UUID;

@Entity
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
    @NotNull
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String zipCode;
    @NotNull
    private String street;
    @NotNull
    private String house;
    private String googleMapsReference;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isThereRefund() {
        return isThereRefund;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
