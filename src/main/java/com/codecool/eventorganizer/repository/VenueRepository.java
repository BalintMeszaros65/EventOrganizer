package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VenueRepository extends JpaRepository<Venue, UUID> {
}
