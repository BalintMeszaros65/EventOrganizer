package com.codecool.eventorganizer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer extends AppUser {
    // TODO billing address later?
    @OneToMany
    List<BookedEvent> bookedEvents;

    public Customer(UUID id, String email, String password, String firstName, String lastName, List<String> roles,
                    List<BookedEvent> bookedEvents) {
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
            bookedEvents = new ArrayList<>();
        }
        bookedEvents.add(bookedEvent);
    }


    // TODO ask if this works as intended (super.equals()) or do I need to use getters and write it manually?
    // generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(bookedEvents, customer.bookedEvents);
    }
    // manual
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Customer customer)) return false;
//        if (!super.equals(o)) return false;
//        return Objects.equals(bookedEvents, customer.bookedEvents) && Objects.equals(this.getId(), customer.getId())
//                && Objects.equals(this.getEmail(), customer.getEmail())
//                && Objects.equals(this.getPassword(), customer.getPassword())
//                && Objects.equals(this.getFirstName(), customer.getFirstName())
//                && Objects.equals(this.getLastName(), customer.getLastName())
//                && Objects.equals(this.getRoles(), customer.getRoles());
//
//    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bookedEvents);
    }
}
