package com.codecool.eventorganizer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends AppUser {
    @OneToMany
    List<BookedEvent> bookedEvents;

    public List<BookedEvent> getBookedEvents() {
        return bookedEvents;
    }

    public BigDecimal calculateAveragePricePaidForOneTicket() {
        if (bookedEvents == null) {
            return BigDecimal.ZERO;
        } else {
            int numberOfTickets = bookedEvents.stream()
                    .map(BookedEvent::getTicketCount)
                    .reduce(0, Integer::sum);
            BigDecimal amountPaid = bookedEvents.stream()
                    .map(BookedEvent::getAmountPayed)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return amountPaid.divide(BigDecimal.valueOf(numberOfTickets), RoundingMode.HALF_UP);
        }
    }

    public void storeBookedEvent(BookedEvent bookedEvent) {
        if (bookedEvents == null) {
            bookedEvents = new ArrayList<>();
        }
        bookedEvents.add(bookedEvent);
    }
}
