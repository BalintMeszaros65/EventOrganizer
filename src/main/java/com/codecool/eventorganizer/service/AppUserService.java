package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.model.BookedEvent;
import com.codecool.eventorganizer.repository.AppUserRepository;
import com.codecool.eventorganizer.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.appUserRepository = appUserRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // basic CRUD operations

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

    //helper methods

    private AppUser getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser savedAppUser = getUserByEmail(email);
        return savedAppUser;
    }

    private void checkIfEmailIsAlreadyRegistered(AppUser appUser) {
        if (appUserRepository.existsByEmail(appUser.getEmail())) {
            throw new CustomExceptions.EmailAlreadyUsedException("Email is already registered.\n");
        }
    }

    private static void checkIfRequiredDataExists(AppUser appUser) {
        String email = appUser.getEmail();
        String password = appUser.getPassword();
        String firstName = appUser.getFirstName();
        String lastName = appUser.getLastName();
        if (email == null ||  password == null || firstName == null || lastName == null
                || "".equals(email) || "".equals(password) || "".equals(firstName) || "".equals(lastName)) {
            throw new CustomExceptions.MissingAttributeException("Missing one or more attribute(s) in AppUser\n");
        }
    }

    private String generateToken(AppUser appUser) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(appUser.getEmail());
        return jwtUtil.generateToken(userDetails);
    }

    private static void checkIfIdDoesNotExist(AppUser appUser) {
        if (appUser.getId() != null) {
            throw new CustomExceptions.IdCanNotExistWhenCreatingEntityException();
        }
    }

    private void checkIfUpdatedInformationIsValid(AppUser appUser) {
        AppUser currentUser = getCurrentUser();
        if (!currentUser.getEmail().equals(appUser.getEmail())) {
            throw new CustomExceptions.EmailCanNotBeChangedException("You can not change your registered email.");
        }
        if (!currentUser.getPassword().equals(appUser.getPassword())) {
            throw new CustomExceptions.PasswordChangeIsDifferentEndpointException(
                    "Changing password is not allowed at this endpoint."
            );
        }
        if (!currentUser.getBookedEvents().equals(appUser.getBookedEvents())) {
            throw new IllegalArgumentException("Changing booked events is not allowed at this endpoint.");
        }
        if ("".equals(appUser.getLastName()) || "".equals(appUser.getFirstName())) {
            throw new CustomExceptions.MissingAttributeException("Last and first name can not be empty.");
        }
    }

    // logic

    public ResponseEntity<String> registerUser(AppUser appUser) {
        checkIfRequiredDataExists(appUser);
        checkIfEmailIsAlreadyRegistered(appUser);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRoles(List.of("ROLE_USER"));
        checkIfIdDoesNotExist(appUser);
        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(generateToken(appUser));
    }

    public ResponseEntity<String> registerOrganizer(AppUser appUser, String secretKey) {
        if (!"organizer".equals(secretKey)) {
            throw new IllegalArgumentException("Secret key for registering an organizer account is not matching.");
        }
        checkIfRequiredDataExists(appUser);
        checkIfEmailIsAlreadyRegistered(appUser);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRoles(List.of("ROLE_ORGANIZER"));
        checkIfIdDoesNotExist(appUser);
        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(generateToken(appUser));
    }

    public ResponseEntity<String> registerAdmin(AppUser appUser, String secretKey) {
        if (!"admin".equals(secretKey)) {
            throw new IllegalArgumentException("Secret key for registering an admin account is not matching.");
        }
        checkIfRequiredDataExists(appUser);
        checkIfEmailIsAlreadyRegistered(appUser);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRoles(List.of("ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"));
        checkIfIdDoesNotExist(appUser);
        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(generateToken(appUser));
    }

    // Only for name, role, billing address
    public ResponseEntity<String> updateUserInformation(AppUser appUser) {
        checkIfUpdatedInformationIsValid(appUser);
        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.OK).body("User information updated.");
    }

    public ResponseEntity<String> changePassword(String newPassword) {
        if ("".equals(newPassword) || newPassword == null) {
            throw new CustomExceptions.MissingAttributeException("Password can not be empty.");
        }
        AppUser currentUser = getCurrentUser();
        if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
            throw new CustomExceptions.MissingAttributeException("New password can not be the old password.");
        }
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body("Password has been changed.");
    }

    public ResponseEntity<String> deleteUser() {
        AppUser currentUser = getCurrentUser();
        appUserRepository.deleteById(currentUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body("User has been deleted.");
    }

    public void addBookedEventToCurrentUser(BookedEvent bookedEvent) {
        AppUser currentUser = getCurrentUser();
        currentUser.storeBookedEvent(bookedEvent);
        appUserRepository.save(currentUser);
    }
}
