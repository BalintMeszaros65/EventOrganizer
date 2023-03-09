package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.exception.CustomExceptions;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<BookedEvent> getBookedEvents() {
        return bookedEvents;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public BigDecimal calculateAveragePricePaidForOneTicket() {
        if (bookedEvents == null) {
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
        if (bookedEvents == null) {
            bookedEvents = new ArrayList<>();
        }
        bookedEvents.add(bookedEvent);
    }


    // TODO move event logic to a new bookingAndRefunding service?
    public void refundBookedEvent(BookedEvent bookedEvent) {
        if (bookedEvent.canBeRefunded() && !bookedEvent.isRefunded()) {
            bookedEvent.setRefunded(true);
            Event event = bookedEvent.getEvent();
            int ticketCount = bookedEvent.getTicketsBought();
            event.refundTicket(ticketCount);
        } else {
            throw new CustomExceptions.EventCanNotBeRefundedException("This booked event can't be refunded.");
        }
    }
}
