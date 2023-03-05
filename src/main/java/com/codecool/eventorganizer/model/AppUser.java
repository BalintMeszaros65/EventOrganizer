package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @Column(unique = true)
    private String email;
    // TODO encode in service
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    // TODO billing address and others for payment later?
    @NotNull
    @ElementCollection
    List<String> roles;
    @NotNull
    @OneToMany
    List<BookedEvent> bookedEvents;

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<BookedEvent> getBookedEvents() {
        return bookedEvents;
    }

    public BigDecimal calculateAveragePricePaidForOneTicket() {
        if (bookedEvents.size() == 0) {
            return BigDecimal.ZERO;
        } else {
            int numberOfTickets = bookedEvents.stream()
                .map(BookedEvent::getTicketsBought)
                .reduce(0, Integer::sum);
            BigDecimal amountPaid = bookedEvents.stream()
                .map(BookedEvent::getAmountPayed)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            return amountPaid.divide(BigDecimal.valueOf(numberOfTickets), RoundingMode.HALF_UP);
        }
    }

    public void bookEvent(BookedEvent bookedEvent) {
        bookedEvents.add(bookedEvent);
    }

    public void refundEvent(BookedEvent bookedEvent) throws Exception {
        if (bookedEvent.canBeRefunded()) {
            bookedEvents.remove(bookedEvent);
        } else {
            // TODO make custom exception(s)
            throw new Exception();
        }
    }
}
