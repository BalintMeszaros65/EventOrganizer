package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.repository.BookedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookedEventService {
    BookedEventRepository bookedEventRepository;

    @Autowired
    public BookedEventService(BookedEventRepository bookedEventRepository) {
        this.bookedEventRepository = bookedEventRepository;
    }

    public void saveAndUpdateBookedEvent(BookedEvent bookedEvent) {
        bookedEventRepository.save(bookedEvent);
    }

    public BookedEvent getBookedEvent(UUID id) {
        Optional<BookedEvent> optionalBookedEvent = bookedEventRepository.findById(id);
        if (optionalBookedEvent.isPresent()) {
            return optionalBookedEvent.get();
        } else {
            throw new NoSuchElementException("Booked event not found by given id.");
        }
    }

    public void deleteBookedEvent(UUID id) {
        bookedEventRepository.deleteById(id);
    }
}
