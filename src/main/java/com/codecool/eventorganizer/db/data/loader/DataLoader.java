package com.codecool.eventorganizer.db.data.loader;

import com.codecool.eventorganizer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class DataLoader implements ApplicationRunner {
    private final AppUserRepository appUserRepository;
    private final BookedEventRepository bookedEventRepository;
    private final EventRepository eventRepository;
    private final GenreRepository genreRepository;
    private final PerformanceRepository performanceRepository;
    private final VenueRepository venueRepository;

    @Autowired
    public DataLoader(AppUserRepository appUserRepository, BookedEventRepository bookedEventRepository,
                      EventRepository eventRepository, GenreRepository genreRepository,
                      PerformanceRepository performanceRepository, VenueRepository venueRepository) {
        this.appUserRepository = appUserRepository;
        this.bookedEventRepository = bookedEventRepository;
        this.eventRepository = eventRepository;
        this.genreRepository = genreRepository;
        this.performanceRepository = performanceRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO add dummy data
    }
}
