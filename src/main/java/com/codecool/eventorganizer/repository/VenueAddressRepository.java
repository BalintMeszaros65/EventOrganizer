package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.VenueAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VenueAddressRepository extends JpaRepository<VenueAddress, UUID> {
}
