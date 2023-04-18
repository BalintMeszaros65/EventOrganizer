package com.codecool.eventorganizer.db.data.loader;

import com.codecool.eventorganizer.model.*;
import com.codecool.eventorganizer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;

@Component
// TODO continue dummy data
public class DataLoader implements CommandLineRunner {
    private final AppUserRepository appUserRepository;
    private final BookedEventRepository bookedEventRepository;
    private final EventRepository eventRepository;
    private final GenreRepository genreRepository;
    private final PerformanceRepository performanceRepository;
    private final VenueRepository venueRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final VenueAddressRepository venueAddressRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public DataLoader(AppUserRepository appUserRepository, BookedEventRepository bookedEventRepository,
                      EventRepository eventRepository, GenreRepository genreRepository,
                      PerformanceRepository performanceRepository, VenueRepository venueRepository,
                      CountryRepository countryRepository, CityRepository cityRepository, VenueAddressRepository venueAddressRepository,
                      CustomerRepository customerRepository) {
        this.appUserRepository = appUserRepository;
        this.bookedEventRepository = bookedEventRepository;
        this.eventRepository = eventRepository;
        this.genreRepository = genreRepository;
        this.performanceRepository = performanceRepository;
        this.venueRepository = venueRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.venueAddressRepository = venueAddressRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    // TODO generate random data
    public void run(String[] args) throws Exception {

        // music dummy data
        // genres
        Genre rock = new Genre(null, "rock", "music");
        rock = genreRepository.save(rock);
        Genre punk = new Genre(null, "punk", "music");
        punk = genreRepository.save(punk);
        Genre punkHardcore = new Genre(null, "punk-hardcore", "music");
        punkHardcore = genreRepository.save(punkHardcore);
        Genre metal = new Genre(null, "metal", "music");
        metal = genreRepository.save(metal);
        Genre drumAndBass = new Genre(null, "drum and bass", "music");
        drumAndBass = genreRepository.save(drumAndBass);
        Genre goa = new Genre(null, "goa", "music");
        goa = genreRepository.save(goa);
        Genre electronicHardcore = new Genre(null, "electronic-hardcore", "music");
        electronicHardcore = genreRepository.save(electronicHardcore);

        // artists
        // punk hc
        Performance fever333 = new Performance(null, "FEVER 333", new URL("https://www.fever333.com"), punkHardcore);
        fever333 = performanceRepository.save(fever333);
        Performance beartooth = new Performance(null, "Beartooth", new URL("https://beartoothband.com"), punkHardcore);
        beartooth = performanceRepository.save(beartooth);
        Performance stickToYourGuns = new Performance(null, "Stick to Your Guns",
                new URL("https://www.facebook.com/STYGoc"), punkHardcore);
        stickToYourGuns = performanceRepository.save(stickToYourGuns);
        Performance teveszme = new Performance(null, "Téveszme", new URL("https://www.facebook.com/teveszme"), punkHardcore);
        teveszme = performanceRepository.save(teveszme);
        Performance hanoi = new Performance(null, "HANØI", new URL("https://www.facebook.com/hanoihardcore"), punkHardcore);
        hanoi = performanceRepository.save(hanoi);
        Performance nemecsek = new Performance(null, "nemecsek", new URL("https://www.facebook.com/nemecsekeger"), punkHardcore);
        nemecsek = performanceRepository.save(nemecsek);


        // DnB
        Performance spor = new Performance(null, "Spor", new URL("https://www.facebook.com/sporlifted"), drumAndBass);
        spor = performanceRepository.save(spor);
        Performance evolIntent = new Performance(null, "Evol Intent", new URL("https://www.facebook.com/realevolintent"), drumAndBass);
        evolIntent = performanceRepository.save(evolIntent);
        Performance badCompanyUk = new Performance(null, "Bad Company UK", new URL("https://www.badcompany.uk.com/home"), drumAndBass);
        badCompanyUk = performanceRepository.save(badCompanyUk);
        Performance mindscape = new Performance(null, "Mindscape", new URL("https://www.facebook.com/m1ndscape"), drumAndBass);
        mindscape = performanceRepository.save(mindscape);
        Performance data3 = new Performance(null, "Data 3", new URL("https://www.facebook.com/data3official"), drumAndBass);
        data3 = performanceRepository.save(data3);
        Performance mobTactics = new Performance(null, "Mob Tactics", new URL("https://www.facebook.com/MobTacticsMusic"), drumAndBass);
        mobTactics = performanceRepository.save(mobTactics);

        // countries
        Country hungary = new Country(null, "Hungary");
        hungary = countryRepository.save(hungary);
        Country austria = new Country(null, "Austria");
        austria = countryRepository.save(austria);
        Country slovakia = new Country(null, "Slovakia");
        slovakia = countryRepository.save(slovakia);

        // cities
        City budapest = new City(null, hungary, "Budapest");
        budapest = cityRepository.save(budapest);
        City vienna = new City(null, austria, "Vienna");
        vienna = cityRepository.save(vienna);
        City bratislava = new City(null, slovakia, "Bratislava");
        bratislava = cityRepository.save(bratislava);

        // addresses
        VenueAddress durerKertAddress = new VenueAddress(null, budapest, "1117", "Öböl utca",
                "1", "https://www.google.com/maps/place/D%C3%BCrer+Kert/@47.4598982,19.0572838,17z/data=!3m1!4b1!4m6!3m5!1s0x4741dc8091c9dc2f:0xecebc630e0349849!8m2!3d47.4598982!4d19.0572838!16s%2Fm%2F0k09tn_");
        durerKertAddress = venueAddressRepository.save(durerKertAddress);
        VenueAddress a38Address = new VenueAddress(null, budapest, "1117", "Petőfi híd", "-",
                "https://www.google.com/maps?ll=47.476639,19.062793&z=16&t=m&hl=hu&gl=HU&mapclient=embed&cid=9961889067075519822");
        a38Address = venueAddressRepository.save(a38Address);
        VenueAddress budapestParkAddress = new VenueAddress(null, budapest, "1095", "Soroksári út", "60",
                "https://www.google.hu/maps/place/Budapest+Park/@47.4676381,19.0745992,17z/data=!3m1!4b1!4m6!3m5!1s0x4741dd1baca87ab9:0x446dd6a1bee0c423!8m2!3d47.4676345!4d19.0767879!16s%2Fg%2F1hhxkxmns?coh=164777&entry=tt");
        budapestParkAddress = venueAddressRepository.save(budapestParkAddress);
        VenueAddress barbaNegraRedStageAddress = new VenueAddress(null, budapest, "1211", "Szállító utca", "3",
                "https://www.google.com/maps?ll=47.441562,19.077528&z=16&t=m&hl=hu&gl=HU&mapclient=embed&cid=3462955035018983980");
        barbaNegraRedStageAddress = venueAddressRepository.save(barbaNegraRedStageAddress);
        VenueAddress gasometersAddress = new VenueAddress(null, vienna, "1110", "Guglgasse", "6",
                "https://www.google.com/maps/place/Gasometers+of+Vienna/@48.1850303,16.4179554,17z/data=!3m1!4b1!4m6!3m5!1s0x476d07552200e63f:0x1fa4253100678110!8m2!3d48.1850303!4d16.4201441!16zL20vMDYzMzk1");
        gasometersAddress = venueAddressRepository.save(gasometersAddress);
        VenueAddress fugaAddress = new VenueAddress(null, bratislava, "811 01", "Námestie SNP", "24",
                "https://www.google.com/maps/place/Fuga/@48.1444756,17.1099126,17z/data=!3m1!4b1!4m6!3m5!1s0x476c8938e3b95c61:0xaba44ffa461d6d3e!8m2!3d48.1444756!4d17.1121013!16s%2Fg%2F11xc8c3np");
        fugaAddress = venueAddressRepository.save(fugaAddress);

        // venues
        Venue durerKertKisTerem = new Venue(null, "Dürer Kert Kisterem", new URL("https://www.durerkert.com"),
                true, 200, durerKertAddress);
        durerKertKisTerem = venueRepository.save(durerKertKisTerem);
        Venue a38 = new Venue(null, "A38", new URL("https://www.a38.hu"), false, 600,
                a38Address);
        a38 = venueRepository.save(a38);
        Venue budapestPark = new Venue(null, "Budapest Park", new URL("https://www.budapestpark.hu"),
                false, 11000, budapestParkAddress);
        budapestPark = venueRepository.save(budapestPark);
        Venue barbaNegraRedStage = new Venue(null, "Barba Negra Red Stage", new URL("https://www.barbanegra.hu"),
                true, 6000, barbaNegraRedStageAddress);
        barbaNegraRedStage = venueRepository.save(barbaNegraRedStage);
        Venue gasometers = new Venue(null, "Gasometers of Vienna", new URL("https://www.wien.info/en/vienna-s-gasometers-133306"),
                false, 4200, gasometersAddress);
        gasometers = venueRepository.save(gasometers);
        Venue fuga = new Venue(null, "Fuga", new URL("https://www.facebook.com/fuga.ba"), false,
                250, fugaAddress);
        fuga = venueRepository.save(fuga);

        // users

        Customer customer1 = new Customer(null, "customer1@gmail.com", "customer1",
                "customer", "1", List.of("ROLE_USER"), null);
        customer1 = customerRepository.save(customer1);
        Customer customer2 = new Customer(null, "customer2@gmail.com", "customer2",
                "customer", "2", List.of("ROLE_USER"), null);
        customer2 = customerRepository.save(customer2);
        AppUser organizer1 = new AppUser(null, "organizer1@gmail.com", "organizer1",
                "organizer", "1", List.of("ROLE_ORGANIZER"));
        organizer1 = appUserRepository.save(organizer1);
        AppUser organizer2 = new AppUser(null, "organizer2@gmail.com", "organizer2",
                "organizer", "2", List.of("ROLE_ORGANIZER"));
        organizer2 = appUserRepository.save(organizer2);
        AppUser admin = new AppUser(null, "admin@gmail.com", "admin",
                "admin", "admin", List.of("ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"));
        admin = appUserRepository.save(admin);

        // events

        Event fever333AtDurerKertKisteremPast = new Event(null, durerKertKisTerem, fever333, organizer1,
                BigDecimal.valueOf(6000), 150, 10, ZonedDateTime.now().minusDays(5),
                90, 2, false);
        fever333AtDurerKertKisteremPast = eventRepository.save(fever333AtDurerKertKisteremPast);
        Event fever333AtDurerKertKisteremFuture = new Event(null, durerKertKisTerem, fever333, organizer1,
                BigDecimal.valueOf(7000), 150, 130, ZonedDateTime.now().plusDays(15),
                80, 2, false);
        fever333AtDurerKertKisteremFuture = eventRepository.save(fever333AtDurerKertKisteremFuture);
        Event fever333AtA38FutureCancelled = new Event(null, a38, fever333, organizer2,
                BigDecimal.valueOf(8000), 450, 450, ZonedDateTime.now().minusDays(3),
                100, 2, true);
        fever333AtA38FutureCancelled = eventRepository.save(fever333AtA38FutureCancelled);
        Event fever333AtBudapestParkFuture = new Event(null, a38, fever333, organizer2,
                BigDecimal.valueOf(8000), 9000, 5487, ZonedDateTime.now().minusDays(3),
                100, 2, false);

        // booked events
    }
}
