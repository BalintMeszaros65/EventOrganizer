package com.codecool.eventorganizer.db.data.loader;

import com.codecool.eventorganizer.model.*;
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
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public DataLoader(AppUserRepository appUserRepository, BookedEventRepository bookedEventRepository,
                      EventRepository eventRepository, GenreRepository genreRepository,
                      PerformanceRepository performanceRepository, VenueRepository venueRepository,
                      CountryRepository countryRepository, CityRepository cityRepository, AddressRepository addressRepository) {
        this.appUserRepository = appUserRepository;
        this.bookedEventRepository = bookedEventRepository;
        this.eventRepository = eventRepository;
        this.genreRepository = genreRepository;
        this.performanceRepository = performanceRepository;
        this.venueRepository = venueRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.addressRepository = addressRepository;
    }

    @Override
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
        Address durerKertAddress = new Address(null, budapest, "1117", "Öböl utca",
                "1", "https://www.google.com/maps/place/D%C3%BCrer+Kert/@47.4598982,19.0572838,17z/data=!3m1!4b1!4m6!3m5!1s0x4741dc8091c9dc2f:0xecebc630e0349849!8m2!3d47.4598982!4d19.0572838!16s%2Fm%2F0k09tn_");
        durerKertAddress = addressRepository.save(durerKertAddress);
        Address a38Address = new Address(null, budapest, "1117", "Petőfi híd", "-",
                "https://www.google.com/maps?ll=47.476639,19.062793&z=16&t=m&hl=hu&gl=HU&mapclient=embed&cid=9961889067075519822");
        a38Address = addressRepository.save(a38Address);
        Address budapestParkAddress = new Address(null, budapest, "1095", "Soroksári út", "60",
                "https://www.google.hu/maps/place/Budapest+Park/@47.4676381,19.0745992,17z/data=!3m1!4b1!4m6!3m5!1s0x4741dd1baca87ab9:0x446dd6a1bee0c423!8m2!3d47.4676345!4d19.0767879!16s%2Fg%2F1hhxkxmns?coh=164777&entry=tt");
        budapestParkAddress = addressRepository.save(budapestParkAddress);
        Address barbaNegraRedStageAddress = new Address(null, budapest, "1211", "Szállító utca", "3",
                "https://www.google.com/maps?ll=47.441562,19.077528&z=16&t=m&hl=hu&gl=HU&mapclient=embed&cid=3462955035018983980");
        barbaNegraRedStageAddress = addressRepository.save(barbaNegraRedStageAddress);
        Address gasometersAddress = new Address(null, vienna, "1110", "Guglgasse", "6",
                "https://www.google.com/maps/place/Gasometers+of+Vienna/@48.1850303,16.4179554,17z/data=!3m1!4b1!4m6!3m5!1s0x476d07552200e63f:0x1fa4253100678110!8m2!3d48.1850303!4d16.4201441!16zL20vMDYzMzk1");
        gasometersAddress = addressRepository.save(gasometersAddress);
        Address fugaAddress = new Address(null, bratislava, "811 01", "Námestie SNP", "24",
                "https://www.google.com/maps/place/Fuga/@48.1444756,17.1099126,17z/data=!3m1!4b1!4m6!3m5!1s0x476c8938e3b95c61:0xaba44ffa461d6d3e!8m2!3d48.1444756!4d17.1121013!16s%2Fg%2F11xc8c3np");
        fugaAddress = addressRepository.save(fugaAddress);

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
        Venue fuga = new Venue(null, "Fuga", new URL("fuga.forumabsurdum.sk"), false,
                250, fugaAddress);
        fuga = venueRepository.save(fuga);
    }
}
