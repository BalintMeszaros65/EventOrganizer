package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.City;
import com.codecool.eventorganizer.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {
    List<City> findAllByCountry(Country country);
}
