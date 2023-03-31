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
import java.util.UUID;

public class EventDto {
    private final UUID id;
    private final Venue venue;
    private final Performance performance;
    private final BigDecimal basePrice;
    private final ZonedDateTime eventStartingDateAndTime;
    private final int eventLengthInMinutes;
    private final List<BookedEvent> overlappedBookedEvents;

    public EventDto(Event event, List<BookedEvent> bookedEventList) {
        this.id = event.getId();
        this.venue = event.getVenue();
        this.performance = event.getPerformance();
        this.basePrice = event.getBasePrice();
        this.eventStartingDateAndTime = event.getEventStartingDateAndTime();
        this.eventLengthInMinutes = event.getEventLengthInMinutes();
        this.overlappedBookedEvents = getOverlappedBookedEvents(event, bookedEventList);
    }

    private List<BookedEvent> getOverlappedBookedEvents(Event event, List<BookedEvent> bookedEventList) {
        ZonedDateTime eventStartingDateAndTime = event.getEventStartingDateAndTime();
        ZonedDateTime eventEndingDateAndTime = eventStartingDateAndTime.plusMinutes(eventLengthInMinutes);
        Interval eventInterval = Interval.of(eventStartingDateAndTime.toInstant(), eventEndingDateAndTime.toInstant());
        List<BookedEvent> overlappedBookedEvents = new ArrayList<>();
        for (BookedEvent bookedEvent : bookedEventList) {
            if (!bookedEvent.isRefunded()) {
                Event eventOfBookedEvent = bookedEvent.getEvent();
                ZonedDateTime bookedEventStartingDateAndTime = eventOfBookedEvent.getEventStartingDateAndTime();
                ZonedDateTime bookedEventEndingDateAndTime = bookedEventStartingDateAndTime
                        .plusMinutes(eventOfBookedEvent.getEventLengthInMinutes());
                Interval bookedEventInterval = Interval.of(bookedEventStartingDateAndTime.toInstant(),
                        bookedEventEndingDateAndTime.toInstant());
                if (eventInterval.overlaps(bookedEventInterval)) {
                    overlappedBookedEvents.add(bookedEvent);
                }
            }
        }
        return overlappedBookedEvents;
    }

}
