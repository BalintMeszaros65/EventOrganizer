package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/api/event/create")
    private ResponseEntity<String> createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }
}
