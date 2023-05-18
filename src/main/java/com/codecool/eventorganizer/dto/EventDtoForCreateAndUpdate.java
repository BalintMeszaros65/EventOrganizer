package com.codecool.eventorganizer.dto;

import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
public class EventDtoForCreateAndUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Null(groups = CreateValidation.class, message = "Id must not exist when creating.")
    @NotNull(groups = UpdateValidation.class)
    private UUID id;
    @NotNull
    private UUID venueId;
    @NotNull
    private UUID performanceId;
    @DecimalMin(value = "0.0", inclusive = false)
    @NotNull
    private BigDecimal basePrice;
    @Positive
    private int ticketsSoldThroughOurApp;
    @Future
    @NotNull
    private ZonedDateTime eventStartingDateAndTime;
    @Positive
    private int eventLengthInMinutes;
    @PositiveOrZero
    private int daysBeforeBookingIsClosed;
}
