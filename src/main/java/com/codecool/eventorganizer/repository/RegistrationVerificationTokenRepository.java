package com.codecool.eventorganizer.repository;

import com.codecool.eventorganizer.model.RegistrationVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationVerificationTokenRepository extends JpaRepository<RegistrationVerificationToken, Long> {
    Optional<RegistrationVerificationToken> findByToken(String token);
}
