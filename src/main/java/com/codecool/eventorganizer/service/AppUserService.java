package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.dto.AppUserDtoForCreate;
import com.codecool.eventorganizer.exception.CustomExceptions;
import com.codecool.eventorganizer.model.*;
import com.codecool.eventorganizer.repository.AppUserRepository;
import com.codecool.eventorganizer.repository.CustomerRepository;
import com.codecool.eventorganizer.repository.RegistrationVerificationTokenRepository;
import com.codecool.eventorganizer.security.JwtUtil;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final CustomerRepository customerRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final RegistrationVerificationTokenRepository verificationTokenRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, CustomerRepository customerRepository,
                          UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService, RegistrationVerificationTokenRepository verificationTokenRepository) {
        this.appUserRepository = appUserRepository;
        this.customerRepository = customerRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
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

    public Customer getCustomerByEmail(String email) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.get();
        } else {
            throw new NoSuchElementException("Customer not found by given id.");
        }
    }

    public String createRegistrationVerificationToken(AppUser appUser) {
        String token = UUID.randomUUID().toString();
        RegistrationVerificationToken registrationVerificationToken = new RegistrationVerificationToken(token, appUser);
        verificationTokenRepository.save(registrationVerificationToken);
        return token;
    }

    public Optional<RegistrationVerificationToken> getRegistrationVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    //helper methods

    public AppUser getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByEmail(email);
    }

    public Customer getCurrentCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getCustomerByEmail(email);
    }

    private void checkIfEmailIsAlreadyRegistered(AppUserDtoForCreate appUserDto) {
        if (appUserRepository.existsByEmail(appUserDto.getEmail())) {
            throw new CustomExceptions.EmailAlreadyUsedException("Email is already registered.\n");
        }
    }

    private String generateToken(AppUser appUser) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(appUser.getEmail());
        return jwtUtil.generateToken(userDetails);
    }

    // logic

    private AppUser createAppUserFromDto(AppUserDtoForCreate appUserDto, List<String> roles, String type) {
        // TODO try to refactor by the sample (below) given on the consultation
        /*
        class Factory {

            private static Map<String, Function<CommonArguments, Something>> registeredFactories = new HashMap<>();

            public static Something getSomething(String marker, Object... args) {
                if (registeredFactories.hasKey(marker)) {
                    var commonArgs = createCommonArgs(args);
                    return registeredFactories.get(marker).apply(commonArgs);
                }
                throw new IllegalArgumentException(String.format("Cannot create something for marker: %s.", marker));
            }

            public void register(String marker, Supplier<Something> creator) {
                registeredFactories.put(marker, creator);
            }
        }


        class CoolSomething extends Something {

            static {
                Factory.register("cool", commonArgs -> {
                        // call ctor
                    });
            }

        }


        public class App {
            public static void main(String[] args) {
                System.out.println(Factory.getSomething("cool"));
            }
        }

         */
        return switch (type) {
            case "admin" -> new AppUser(null, appUserDto.getEmail(), passwordEncoder.encode(appUserDto.getPassword()),
                    appUserDto.getFirstName(), appUserDto.getLastName(), roles, false);
            case "organizer" -> new Organizer(appUserDto.getEmail(), passwordEncoder.encode(appUserDto.getPassword()),
                    appUserDto.getFirstName(), appUserDto.getLastName(), roles);
            case "customer" -> new Customer(appUserDto.getEmail(), passwordEncoder.encode(appUserDto.getPassword()),
                    appUserDto.getFirstName(), appUserDto.getLastName(), roles);
            default -> throw new IllegalArgumentException("AppUser type not found");
        };
    }

    public ResponseEntity<String> registerCustomer(AppUserDtoForCreate appUserDto) {
        checkIfEmailIsAlreadyRegistered(appUserDto);
        AppUser appUser = createAppUserFromDto(appUserDto, List.of("ROLE_USER"), "customer");
        AppUser savedAppUser = appUserRepository.save(appUser);
        String token = createRegistrationVerificationToken(savedAppUser);
        emailService.sendEmail(savedAppUser.getEmail(), "Registration to Event Organizer",
                "Please confirm your registration to Event Organizer" + "\r\n" +
                        "http://localhost:8080/api/user/register/confirm?token=" + token);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created, confirmation email sent.");
    }

    public ResponseEntity<String> registerOrganizer(AppUserDtoForCreate appUserDto, String secretKey) {
        // TODO change placeholder to generated secret key
        if (!"organizer".equals(secretKey)) {
            throw new IllegalArgumentException("Secret key for registering an organizer account is not matching.");
        }
        checkIfEmailIsAlreadyRegistered(appUserDto);
        AppUser appUser = createAppUserFromDto(appUserDto, List.of("ROLE_ORGANIZER"), "organizer");
        AppUser savedAppUser = appUserRepository.save(appUser);
        String token = createRegistrationVerificationToken(savedAppUser);
        emailService.sendEmail(savedAppUser.getEmail(), "Registration to Event Organizer",
                "Please confirm your registration to Event Organizer" + "\r\n" +
                        "http://localhost:8080/api/user/register/confirm?token=" + token);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created, confirmation email sent.");
    }

    public ResponseEntity<String> registerAdmin(AppUserDtoForCreate appUserDto, String secretKey) {
        // TODO change placeholder to generated secret key
        if (!"admin".equals(secretKey)) {
            throw new IllegalArgumentException("Secret key for registering an admin account is not matching.");
        }
        checkIfEmailIsAlreadyRegistered(appUserDto);
        AppUser appUser = createAppUserFromDto(appUserDto, List.of("ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"), "admin");
        AppUser savedAppUser = appUserRepository.save(appUser);
        String token = createRegistrationVerificationToken(savedAppUser);
        emailService.sendEmail(savedAppUser.getEmail(), "Registration to Event Organizer",
                "Please confirm your registration to Event Organizer" + "\r\n" +
                        "http://localhost:8080/api/user/register/confirm?token=" + token);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created, confirmation email sent.");
    }

    public ResponseEntity<String> confirmRegistration(String tokenString) {
        Optional<RegistrationVerificationToken> optionalToken = getRegistrationVerificationToken(tokenString);
        if (optionalToken.isEmpty()) {
            throw new IllegalArgumentException("Invalid confirmation token.");
        }
        RegistrationVerificationToken token = optionalToken.get();
        if (token.getExpirationDateTime().isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException("Confirmation token expired.");
        }
        AppUser appUser = token.getAppUser();
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
        return ResponseEntity.status(HttpStatus.OK).body(generateToken(appUser));
    }

    public ResponseEntity<String> updateUserName(@NotBlank String firstName, @NotBlank String lastName) {
        AppUser currentUser = getCurrentUser();
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        appUserRepository.save(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body("User information updated.");
    }

    public ResponseEntity<String> changePassword(String newPassword) {
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

    public void addBookedEventToCurrentCustomer(BookedEvent bookedEvent) {
        Customer customer = getCurrentCustomer();
        customer.storeBookedEvent(bookedEvent);
        customerRepository.save(customer);
    }
}
