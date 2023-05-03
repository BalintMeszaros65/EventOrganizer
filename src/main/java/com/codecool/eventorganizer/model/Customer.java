package com.codecool.eventorganizer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer extends AppUser {
    // TODO billing address later?
    @OneToMany
    Set<BookedEvent> bookedEvents;

    public Customer(UUID id, String email, String password, String firstName, String lastName, List<String> roles,
                    Set<BookedEvent> bookedEvents) {
        super(id, email, password, firstName, lastName, roles);
        this.bookedEvents = bookedEvents;
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
            bookedEvents = new HashSet<>();
        }
        bookedEvents.add(bookedEvent);
    }
}
