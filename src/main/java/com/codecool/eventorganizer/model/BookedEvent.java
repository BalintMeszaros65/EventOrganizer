package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class BookedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @ManyToOne
    private Event event;
    @NotNull
    private BigDecimal amountPayed;
    @NotNull
    private int ticketsBought;
    @NotNull
    private LocalDateTime dateOfBooking;

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmountPayed() {
        return amountPayed;
    }

    public int getTicketsBought() {
        return ticketsBought;
    }

    // TODO make custom exception(s)
    public void refund() throws Exception {
        event.refundTicket(ticketsBought);
    }
}
