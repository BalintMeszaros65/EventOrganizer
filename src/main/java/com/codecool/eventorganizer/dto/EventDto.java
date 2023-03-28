package com.codecool.eventorganizer.dto;

import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.model.Venue;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class EventDto {
    private Venue venue;
    private Performance performance;
    private BigDecimal basePrice;
    private ZonedDateTime eventStartingDateAndTime;
    private double eventLengthInHours;
    boolean isConflictingWithUsersEvents;

    public EventDto(Event event, boolean isConflictingWithUsersEvents) {
        this.venue = event.getVenue();
        this.performance = event.getPerformance();
        this.basePrice = event.getBasePrice();
        this.eventStartingDateAndTime = event.getEventStartingDateAndTime();
        this.eventLengthInHours = event.getEventLengthInHours();
        this.isConflictingWithUsersEvents = isConflictingWithUsersEvents;
    }
}
