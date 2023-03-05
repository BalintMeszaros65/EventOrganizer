package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.BookedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookedEventRepository extends JpaRepository<BookedEvent, UUID> {
}
