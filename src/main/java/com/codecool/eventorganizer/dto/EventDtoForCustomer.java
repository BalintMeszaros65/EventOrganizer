package com.codecool.eventorganizer.dto;

import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.model.Venue;
import lombok.Getter;
import lombok.Setter;
import org.threeten.extra.Interval;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class EventDtoForCustomer {
    private final UUID id;
    private final Venue venue;
    private final Performance performance;
    private final BigDecimal basePrice;
    private final ZonedDateTime eventStartingDateAndTime;
    private final int eventLengthInMinutes;
    private final Set<BookedEvent> overlappedBookedEvents;

    /* creates a DTO from Event and stores all Booked Events overlapping it (if there is any),
     so the customer can be informed about it */
    public EventDtoForCustomer(Event event, Set<BookedEvent> bookedEventList) {
        this.id = event.getId();
        this.venue = event.getVenue();
        this.performance = event.getPerformance();
        this.basePrice = event.getBasePrice();
        this.eventStartingDateAndTime = event.getEventStartingDateAndTime();
        this.eventLengthInMinutes = event.getEventLengthInMinutes();
        this.overlappedBookedEvents = getOverlappedBookedEvents(event, bookedEventList);
    }

    private Set<BookedEvent> getOverlappedBookedEvents(Event event, Set<BookedEvent> bookedEventList) {
        ZonedDateTime eventStartingDateAndTime = event.getEventStartingDateAndTime();
        ZonedDateTime eventEndingDateAndTime = eventStartingDateAndTime.plusMinutes(eventLengthInMinutes);
        Interval eventInterval = Interval.of(eventStartingDateAndTime.toInstant(), eventEndingDateAndTime.toInstant());
        Set<BookedEvent> overlappedBookedEvents = new HashSet<>();
        for (BookedEvent bookedEvent : bookedEventList) {
            if (!bookedEvent.isRefunded()) {
                Event eventOfBookedEvent = bookedEvent.getEvent();
                ZonedDateTime bookedEventStartingDateAndTime = eventOfBookedEvent.getEventStartingDateAndTime();
                ZonedDateTime bookedEventEndingDateAndTime = bookedEventStartingDateAndTime
                        .plusMinutes(eventOfBookedEvent.getEventLengthInMinutes());
                Interval bookedEventInterval = Interval.of(bookedEventStartingDateAndTime.toInstant(),
                        bookedEventEndingDateAndTime.toInstant());
                if (eventInterval.overlaps(bookedEventInterval) && !eventOfBookedEvent.equals(event)) {
                    overlappedBookedEvents.add(bookedEvent);
                }
            }
        }
        return overlappedBookedEvents;
    }

}
