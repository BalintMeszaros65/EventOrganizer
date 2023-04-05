package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.dto.EventDto;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.service.EventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@SecurityRequirement(name = "Basic Authentication")
@Tag(
        name = "Event",
        description = "Operations about event"
)
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Secured("ROLE_USER")
    @GetMapping("/api/event/user/get-events")
    public List<EventDto> getUpcomingEventsForUser() {
        return eventService.getUpcomingEventsForUser();
    }

    @Secured("ROLE_ORGANIZER")
    @GetMapping("/api/event/organizer/get-events")
    public List<Event> getAllEventsByOrganizer() {
        return eventService.getAllEventsByOrganizer();
    }

    @Secured("ROLE_ORGANIZER")
    @PostMapping("/api/event/create")
    public ResponseEntity<String> createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @Secured("ROLE_ORGANIZER")
    @PutMapping("/api/event/update")
    public ResponseEntity<String> updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/event/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable UUID id) {
        return eventService.deleteEvent(id);
    }
}
