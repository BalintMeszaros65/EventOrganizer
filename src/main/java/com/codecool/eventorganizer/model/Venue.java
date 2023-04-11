package com.codecool.eventorganizer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
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
}
