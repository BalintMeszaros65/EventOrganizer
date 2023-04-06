package com.codecool.eventorganizer.db.data.loader;

import com.codecool.eventorganizer.model.Genre;
import com.codecool.eventorganizer.model.Performance;
import com.codecool.eventorganizer.model.Venue;
import com.codecool.eventorganizer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
// TODO continue dummy data
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
        Genre electronicHardcore = new Genre("electronic-hardcore", "music");
        electronicHardcore = genreRepository.save(electronicHardcore);

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

        // venues

        Venue durerKertKisTerem = new Venue("Dürer Kert Kisterem", new URL("https://www.durerkert.com"),
                true, 200, "Hungary", "Budapest", "1117", "Öböl utca",
                "1", "https://www.google.com/maps/place/D%C3%BCrer+Kert/@47.4598982,19.0572838,17z/data=!3m1!4b1!4m6!3m5!1s0x4741dc8091c9dc2f:0xecebc630e0349849!8m2!3d47.4598982!4d19.0572838!16s%2Fm%2F0k09tn_");
        durerKertKisTerem = venueRepository.save(durerKertKisTerem);
        Venue a38 = new Venue("A38", new URL("https://www.a38.hu"), false, 600,
                "Hungary", "Budapest", "1117", "Petőfi híd", "-",
                "https://www.google.com/maps?ll=47.476639,19.062793&z=16&t=m&hl=hu&gl=HU&mapclient=embed&cid=9961889067075519822");
        a38 = venueRepository.save(a38);
        Venue budapestPark = new Venue("Budapest Park", new URL("https://www.budapestpark.hu"),
                false, 11000, "Hungary", "Budapest", "1095", "Soroksári út",
                "60", "https://www.google.hu/maps/place/Budapest+Park/@47.4676381,19.0745992,17z/data=!3m1!4b1!4m6!3m5!1s0x4741dd1baca87ab9:0x446dd6a1bee0c423!8m2!3d47.4676345!4d19.0767879!16s%2Fg%2F1hhxkxmns?coh=164777&entry=tt");
        budapestPark = venueRepository.save(budapestPark);
        Venue barbaNegraRedStage = new Venue("Barba Negra Red Stage", new URL("https://www.barbanegra.hu"),
                true, 6000, "Hungary", "Budapest", "1211", "Szállító utca",
                "3", "https://www.google.com/maps?ll=47.441562,19.077528&z=16&t=m&hl=hu&gl=HU&mapclient=embed&cid=3462955035018983980");
        barbaNegraRedStage = venueRepository.save(barbaNegraRedStage);
    }
}
