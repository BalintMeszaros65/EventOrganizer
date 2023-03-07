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

    public AppUser getUserByEmail(String email) {
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email);
        if (optionalAppUser.isPresent()) {
            return optionalAppUser.get();
        } else {
            throw new NoSuchElementException("User not found by given id.");
        }
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


    // TODO logic to verify after security
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

    // TODO logic to verify after security
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

    public ResponseEntity<String> loginUser(AppUser appUser) {
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(appUser.getEmail());
        if (optionalAppUser.isPresent()) {
            AppUser savedAppUser = optionalAppUser.get();
            if (passwordEncoder.matches(appUser.getPassword(), savedAppUser.getPassword())) {
                // TODO token creation after security
                String token = "placeholder";
                return ResponseEntity.status(HttpStatus.OK).body(token);
            }
        }
        throw new CustomExceptions.MissingAttributeException("Unable to login");
    }

    private boolean IsEmailAlreadyInUse(String email) {
        return appUserRepository.existsByEmail(email);
    }

    public ResponseEntity<String> updateUserInformation(AppUser appUser) {
        // TODO change id getter to work from context after security
        AppUser savedAppUser = getUserById(appUser.getId());
        if (!savedAppUser.getEmail().equals(appUser.getEmail())) {
            throw new CustomExceptions.EmailCanNotBeChangedException("You can not change your registered email.");
        }
        if (!savedAppUser.getPassword().equals(appUser.getPassword())) {
            throw new CustomExceptions.PasswordChangeIsDifferentEndpointException("Password change is" +
                    "not allowed at this endpoint.");
        }
        saveAndUpdateUser(appUser);
        return ResponseEntity.status(HttpStatus.OK).body("User information updated.");
    }

    public ResponseEntity<String> changePassword(String newPassword) {
        // TODO after security
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("To be implemented.");
    }

    public ResponseEntity<String> deleteUser() {
        // TODO after security
        // appUserRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("To be implemented.");
    }
}
