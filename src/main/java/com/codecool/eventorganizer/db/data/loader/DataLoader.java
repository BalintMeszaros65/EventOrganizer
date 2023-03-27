package com.codecool.eventorganizer.db.data.loader;

import com.codecool.eventorganizer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class DataLoader implements ApplicationRunner {
    private final AppUserService appUserService;
    private final BookedEventService bookedEventService;
    private final EventService eventService;
    private final GenreService genreService;
    private final PerformanceService performanceService;
    private final VenueService venueService;

    @Autowired
    public DataLoader(AppUserService appUserService, BookedEventService bookedEventService, EventService eventService,
                      GenreService genreService, PerformanceService performanceService, VenueService venueService) {
        this.appUserService = appUserService;
        this.bookedEventService = bookedEventService;
        this.eventService = eventService;
        this.genreService = genreService;
        this.performanceService = performanceService;
        this.venueService = venueService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO add dummy data
    }
}
