package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/api/event/create")
    public ResponseEntity<String> createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PutMapping("/api/event/update")
    public ResponseEntity<String> updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    // Admin role only!
    @DeleteMapping("/api/event/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable UUID id) {
        return eventService.deleteEvent(id);
    }
}
