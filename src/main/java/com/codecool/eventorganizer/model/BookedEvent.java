package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.utility.CreateValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Null(groups = CreateValidation.class, message = "Id must not exist when creating.")
    private UUID id;
    @NotNull
    @ManyToOne
    private Event event;
    @DecimalMin(value = "0.0", inclusive = false)
    @NotNull
    private BigDecimal amountPayed;
    @Positive
    private int ticketCount;
    @NotNull
    @PastOrPresent
    private ZonedDateTime dateOfBooking;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser appUser;
    @AssertFalse(groups = CreateValidation.class, message = "BookedEvent can not be refunded when creating.")
    private boolean isRefunded;

    public BookedEvent(Event event, BigDecimal amountPayed, int ticketCount, AppUser appUser) {
        this.event = event;
        this.amountPayed = amountPayed;
        this.ticketCount = ticketCount;
        this.appUser = appUser;
        this.dateOfBooking = ZonedDateTime.now();
        this.isRefunded = false;
    }

    public Genre getGenre() {
        return event.getGenre();
    }

    public boolean canBeRefunded() {
        return event.canBeRefunded() && !isRefunded;
    }

    public void refund() {
        if (!canBeRefunded()) {
            throw new CustomExceptions.IllegalEventStateException("Booked event can not be refunded.");
        }
        // TODO payment later?
        isRefunded = true;
    }

    public void refundByEventOrganizer() {
        if (!isRefunded) {
            // TODO payment later?
            isRefunded = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookedEvent that)) return false;
        return getTicketCount() == that.getTicketCount() && isRefunded() == that.isRefunded()
                && Objects.equals(getId(), that.getId()) && Objects.equals(getEvent(), that.getEvent())
                && Objects.equals(getAmountPayed(), that.getAmountPayed())
                && Objects.equals(getDateOfBooking(), that.getDateOfBooking())
                && Objects.equals(getAppUser(), that.getAppUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEvent(), getAmountPayed(), getTicketCount(), getDateOfBooking(), getAppUser(),
                isRefunded());
    }
}
