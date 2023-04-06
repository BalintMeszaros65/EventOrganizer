package com.codecool.eventorganizer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class AppUser {
    // TODO make a child (Customer) with bookedEvents so the organizer and admin doesn't have to store it with null
    // TODO billing address and others for payment later in Customer?
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @Column(unique = true)
    @Schema(example = "testemail@gmail.com")
    private String email;
    @NotNull
    @Schema(example = "testpassword")
    private String password;
    @NotNull
    @Schema(example = "John")
    private String firstName;
    @NotNull
    @Schema(example = "Smith")
    private String lastName;
    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    List<String> roles;
    @OneToMany
    List<BookedEvent> bookedEvents;

    public AppUser() {

    }

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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
