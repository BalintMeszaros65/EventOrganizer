package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    Set<Event> findAllByPerformance(Performance performance);
    @Query("""
    SELECT event FROM Event event
    WHERE event.isCancelled = false AND event.eventStartingDateAndTime > :zonedDateTime
    """)
    Set<Event> findAllNotCancelledAfterZonedDateTime(ZonedDateTime zonedDateTime);
    Set<Event> findAllByOrganizer(AppUser organizer);
}
