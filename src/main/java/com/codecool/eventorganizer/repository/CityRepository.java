package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.City;
import com.codecool.eventorganizer.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {
    Set<City> findAllDistinctByCountry(Country country);
}
