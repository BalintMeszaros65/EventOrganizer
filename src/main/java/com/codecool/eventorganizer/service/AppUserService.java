package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveAndUpdateUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    public AppUser getUserById(UUID id) {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
        if (optionalAppUser.isPresent()) {
            return optionalAppUser.get();
        } else {
            throw new NoSuchElementException("User not found by given id.");
        }
    }

    public void deleteUser(UUID id) {
        appUserRepository.deleteById(id);
    }

    public ResponseEntity<String> registerUser(AppUser appUser) {
        if (appUser.getEmail() == null || appUser.getPassword() == null || appUser.getFirstName() == null
                || appUser.getLastName() == null) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in AppUser\n");
        } else {
            if (IsEmailAlreadyInUse(appUser.getEmail())) {
                throw new CustomExceptions.EmailAlreadyUsedException("Email is already registered.\n");
            } else {
                appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
                appUser.setRoles(List.of("ROLE_USER"));
                saveAndUpdateUser(appUser);
            }
        }
        // TODO token creation after security
        String token = "placeholder";
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    public ResponseEntity<String> registerOrganizer(AppUser appUser) {
        if (appUser.getEmail() == null || appUser.getPassword() == null || appUser.getFirstName() == null
                || appUser.getLastName() == null) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in AppUser\n");
        } else {
            if (IsEmailAlreadyInUse(appUser.getEmail())) {
                throw new CustomExceptions.EmailAlreadyUsedException("Email is already registered.\n");
            } else {
                appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
                appUser.setRoles(List.of("ROLE_ORGANIZER"));
                saveAndUpdateUser(appUser);
            }
        }
        // TODO token creation after security
        String token = "placeholder";
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    public ResponseEntity<String> registerAdmin(AppUser appUser) {
        if (appUser.getEmail() == null || appUser.getPassword() == null || appUser.getFirstName() == null
                || appUser.getLastName() == null) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in AppUser\n");
        } else {
            if (IsEmailAlreadyInUse(appUser.getEmail())) {
                throw new CustomExceptions.EmailAlreadyUsedException("Email is already registered.\n");
            } else {
                appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
                appUser.setRoles(List.of("ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"));
                saveAndUpdateUser(appUser);
            }
        }
        // TODO token creation after security
        String token = "placeholder";
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    private boolean IsEmailAlreadyInUse(String email) {
        return appUserRepository.existsByEmail(email);
    }
}
