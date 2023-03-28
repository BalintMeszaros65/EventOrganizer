package com.codecool.eventorganizer.db.data.loader;

import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class DataLoader implements CommandLineRunner {
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
    public void run(String[] args) throws Exception {

        // music dummy data
        // genres

        Genre rock = new Genre("rock", "music");
        rock = genreRepository.save(rock);
        Genre punk = new Genre("punk", "music");
        punk = genreRepository.save(punk);
        Genre punkHardcore = new Genre("punk-hardcore", "music");
        punkHardcore = genreRepository.save(punkHardcore);
        Genre metal = new Genre("metal", "music");
        metal = genreRepository.save(metal);
        Genre drumAndBass = new Genre("drum and bass", "music");
        drumAndBass = genreRepository.save(drumAndBass);
        Genre goa = new Genre("goa", "music");
        goa = genreRepository.save(goa);
        Genre electricHardcore = new Genre("electric-hardcore", "music");
        electricHardcore = genreRepository.save(electricHardcore);

        // artists
        // punk hc

        Performance fever333 = new Performance("FEVER 333", new URL("https://www.fever333.com"), punkHardcore);
        fever333 = performanceRepository.save(fever333);
        Performance beartooth = new Performance("Beartooth", new URL("https://beartoothband.com"), punkHardcore);
        beartooth = performanceRepository.save(beartooth);
        Performance stickToYourGuns = new Performance("Stick to Your Guns",
                new URL("https://www.facebook.com/STYGoc"), punkHardcore);
        stickToYourGuns = performanceRepository.save(stickToYourGuns);
        Performance teveszme = new Performance("Téveszme", new URL("https://www.facebook.com/teveszme"), punkHardcore);
        teveszme = performanceRepository.save(teveszme);
        Performance hanoi = new Performance("HANØI", new URL("https://www.facebook.com/hanoihardcore"), punkHardcore);
        hanoi = performanceRepository.save(hanoi);
        Performance nemecsek = new Performance("nemecsek", new URL("https://www.facebook.com/nemecsekeger"), punkHardcore);
        nemecsek = performanceRepository.save(nemecsek);


        // DnB

        Performance spor = new Performance("Spor", new URL("https://www.facebook.com/sporlifted"), drumAndBass);
        spor = performanceRepository.save(spor);
        Performance evolIntent = new Performance("Evol Intent", new URL("https://www.facebook.com/realevolintent"), drumAndBass);
        evolIntent = performanceRepository.save(evolIntent);
        Performance badCompanyUk = new Performance("Bad Company UK", new URL("https://www.badcompany.uk.com/home"), drumAndBass);
        badCompanyUk = performanceRepository.save(badCompanyUk);
        Performance mindscape = new Performance("Mindscape", new URL("https://www.facebook.com/m1ndscape"), drumAndBass);
        mindscape = performanceRepository.save(mindscape);
        Performance data3 = new Performance("Data 3", new URL("https://www.facebook.com/data3official"), drumAndBass);
        data3 = performanceRepository.save(data3);
        Performance mobTactics = new Performance("Mob Tactics", new URL("https://www.facebook.com/MobTacticsMusic"), drumAndBass);
        mobTactics = performanceRepository.save(mobTactics);
    }
}
