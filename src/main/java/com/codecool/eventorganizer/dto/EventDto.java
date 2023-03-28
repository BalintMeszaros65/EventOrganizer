package com.codecool.eventorganizer.dto;

import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.model.Venue;
import org.threeten.extra.Interval;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDto {
    private final Venue venue;
    private final Performance performance;
    private final BigDecimal basePrice;
    private final ZonedDateTime eventStartingDateAndTime;
    private final double eventLengthInHours;
    private final List<BookedEvent> overlappedBookedEvents;

    public EventDto(Event event, List<BookedEvent> bookedEventList) {
        this.venue = event.getVenue();
        this.performance = event.getPerformance();
        this.basePrice = event.getBasePrice();
        this.eventStartingDateAndTime = event.getEventStartingDateAndTime();
        this.eventLengthInHours = event.getEventLengthInHours();
        this.overlappedBookedEvents = getOverlappedBookedEvents(event, bookedEventList);
    }

    private List<BookedEvent> getOverlappedBookedEvents(Event event, List<BookedEvent> bookedEventList) {
        ZonedDateTime eventStartingDateAndTime = event.getEventStartingDateAndTime();
        ZonedDateTime eventEndingDateAndTime = eventStartingDateAndTime.plusMinutes(Math.round(eventLengthInHours * 60.0));
        Interval eventInterval = Interval.of(eventEndingDateAndTime.toInstant(), eventEndingDateAndTime.toInstant());
        List<BookedEvent> overlappedBookedEvents = new ArrayList<>();
        for (BookedEvent bookedEvent : bookedEventList) {
            Event eventOfBookedEvent = bookedEvent.getEvent();
            ZonedDateTime bookedEventStartingDateAndTime = eventOfBookedEvent.getEventStartingDateAndTime();
            ZonedDateTime bookedEventEndingDateAndTime = bookedEventStartingDateAndTime
                    .plusMinutes(Math.round(eventOfBookedEvent.getEventLengthInHours() * 60.0));
            Interval bookedEventInterval = Interval.of(bookedEventStartingDateAndTime.toInstant(),
                    bookedEventEndingDateAndTime.toInstant());
            if (eventInterval.overlaps(bookedEventInterval)) {
                overlappedBookedEvents.add(bookedEvent);
            }
        }
        return overlappedBookedEvents;
    }

}
