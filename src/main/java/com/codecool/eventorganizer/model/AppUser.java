package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @Column(unique = true)
    private String username;
    // TODO encode in service
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @ElementCollection
    List<String> roles;
    @NotNull
    @OneToMany
    List<BookedEvent> bookedEvents;

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
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

    public BigDecimal calculateAveragePayedPriceOfTicket() {
        if (bookedEvents.size() == 0) {
            return BigDecimal.ZERO;
        } else {
            return bookedEvents.stream()
                    .map(BookedEvent::getAmountPayed)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(bookedEvents.size()), RoundingMode.HALF_UP);
        }
    }
}
