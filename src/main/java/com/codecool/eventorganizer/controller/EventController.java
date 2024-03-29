package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.dto.EventDtoForCreateAndUpdate;
import com.codecool.eventorganizer.dto.EventDtoForCustomer;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.service.EventService;
import com.codecool.eventorganizer.utility.CreateValidation;
import com.codecool.eventorganizer.utility.UpdateValidation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Basic Authentication")
@Tag(
        name = "Event",
        description = "Operations about event"
)
@Validated
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Secured("ROLE_USER")
    @GetMapping("/api/event/get-events/customer")
    public Set<EventDtoForCustomer> getUpcomingEventsForCustomer() {
        return eventService.getUpcomingEventsForCustomer();
    }

    @Secured("ROLE_ORGANIZER")
    @GetMapping("/api/event/get-events/organizer")
    public Set<Event> getAllEventsByOrganizer() {
        return eventService.getAllEventsByCurrentOrganizer();
    }

    @Secured("ROLE_ORGANIZER")
    @PostMapping("/api/event/create")
    public ResponseEntity<String> createEvent(@Validated(CreateValidation.class)
                                                  @RequestBody EventDtoForCreateAndUpdate eventDtoForCreateAndUpdate) {
        return eventService.createEvent(eventDtoForCreateAndUpdate);
    }

    @Secured("ROLE_ORGANIZER")
    @PutMapping("/api/event/update")
    public ResponseEntity<String> updateEvent(@Validated(UpdateValidation.class)
                                                  @RequestBody EventDtoForCreateAndUpdate eventDtoForCreateAndUpdate) {
        return eventService.updateEvent(eventDtoForCreateAndUpdate);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/event/delete/{id}")
    public ResponseEntity<String> deleteEvent(@NotNull @PathVariable UUID id) {
        return eventService.deleteEvent(id);
    }
}
