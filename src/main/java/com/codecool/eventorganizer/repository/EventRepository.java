package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query("""
    SELECT event FROM Event event
    WHERE event.isCancelled = false AND event.performance = :performance AND event.eventStartingDateAndTime > :now
    """)
    Set<Event> findAllUpcomingAndValidByPerformance(Performance performance, ZonedDateTime now);
    @Query("""
    SELECT event FROM Event event
    WHERE event.isCancelled = false AND event.organizer = :organizer AND event.eventStartingDateAndTime > :now
    """)
    Set<Event> findAllUpcomingAndValidByOrganizer(AppUser organizer, ZonedDateTime now);
    @Query("""
    SELECT event FROM Event event
    WHERE event.isCancelled = false AND event.performance.genre = :genre AND event.eventStartingDateAndTime > :now
    """)
    Set<Event> findAllUpcomingAndValidByGenre(Genre genre, ZonedDateTime now);
    @Query("""
    SELECT event FROM Event event
    WHERE event.isCancelled = false AND event.venue = :venue AND event.eventStartingDateAndTime > :now
    """)
    Set<Event> findAllUpcomingAndValidByVenue(Venue venue, ZonedDateTime now);
    @Query("""
    SELECT event FROM Event event
    WHERE event.isCancelled = false AND event.venue.venueAddress.city = :city AND event.eventStartingDateAndTime > :now
    """)
    Set<Event> findAllUpcomingAndValidByCity(City city, ZonedDateTime now);
    @Query("""
    SELECT event FROM Event event
    WHERE event.isCancelled = false AND event.venue.venueAddress.city.country = :country AND event.eventStartingDateAndTime > :now
    """)
    Set<Event> findAllUpcomingAndValidByCountry(Country country, ZonedDateTime now);
    @Query("""
    SELECT event FROM Event event
    WHERE event.isCancelled = false AND event.eventStartingDateAndTime > :now
    """)
    Set<Event> findAllUpcomingAndValid(ZonedDateTime now);
    Set<Event> findAllByOrganizer(AppUser organizer);


}
