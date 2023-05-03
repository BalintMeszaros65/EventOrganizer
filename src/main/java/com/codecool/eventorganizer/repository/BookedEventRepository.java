package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface BookedEventRepository extends JpaRepository<BookedEvent, UUID> {
    Set<BookedEvent> findAllByEvent(Event event);
}
