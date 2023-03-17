package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.model.Event;
import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.repository.BookedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookedEventService {
    private final BookedEventRepository bookedEventRepository;
    private final EventService eventService;
    private final AppUserService appUserService;
    private final GenreService genreService;

    @Autowired
    public BookedEventService(BookedEventRepository bookedEventRepository, EventService eventService, AppUserService appUserService, GenreService genreService) {
        this.bookedEventRepository = bookedEventRepository;
        this.eventService = eventService;
        this.appUserService = appUserService;
        this.genreService = genreService;
    }

    // basic CRUD operations

    public List<BookedEvent> getBookedEventsByEvent(Event event) {
        return bookedEventRepository.findAllByEvent(event);
    }

    // helper methods

    private void checkIfRequiredDataExists(BookedEvent bookedEvent) {
        Event event = bookedEvent.getEvent();
        Genre genre = bookedEvent.getGenre();
        int ticketsBooked = bookedEvent.getTicketsCount();
        BigDecimal amountPayed = bookedEvent.getAmountPayed();
        if (event == null || amountPayed == null || BigDecimal.ZERO.equals(amountPayed) || ticketsBooked <= 0
             || bookedEvent.getDateOfBooking() == null || genre == null ||
                bookedEvent.isRefunded()) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in booked event.");
        }
        if (!event.canBeBooked(ticketsBooked)) {
            throw new CustomExceptions.EventCanNotBeBookedException("Event can not be booked.");
        }
        Event savedEvent = eventService.getEvent(event.getId());
        if (!event.equals(savedEvent)) {
            throw new IllegalArgumentException("Event's data does not match with the one in database.");
        }
        Genre savedGenre = genreService.getGenreById(genre.getId());
        if (!genre.equals(savedGenre)) {
            throw new IllegalArgumentException("Genre's data does not match with the one in database.");
        }
    }

    private AppUser getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserService.getUserByEmail(email);
    }

    private void checkIfCurrentUserEqualsBookedEventUser(BookedEvent bookedEvent) {
        if (!getCurrentUser().equals(bookedEvent.getAppUser())) {
            throw new CustomExceptions.CurrentUserIsNotTheOneWhoBookedTheEventException();
        }
    }

    // logic

    public BookedEvent saveBookedEvent(BookedEvent bookedEvent) {
        checkIfRequiredDataExists(bookedEvent);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserService.getUserByEmail(email);
        bookedEvent.setAppUser(appUser);
        if (bookedEvent.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
        return bookedEventRepository.save(bookedEvent);
    }
}
