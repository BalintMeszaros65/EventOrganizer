package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PerformanceRepository extends JpaRepository<Performance, UUID> {
}
