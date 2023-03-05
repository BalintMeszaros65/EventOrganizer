package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
