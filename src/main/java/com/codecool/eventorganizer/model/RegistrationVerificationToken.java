package com.codecool.eventorganizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationVerificationToken {
    private static final int DAYS_UNTIL_EXPIRATION = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    @OneToOne
    @NotNull
    private AppUser appUser;
    private ZonedDateTime expirationDateTime;

    public RegistrationVerificationToken(String token, AppUser appUser) {
        this.token = token;
        this.appUser = appUser;
        this.expirationDateTime = ZonedDateTime.now().plusDays(DAYS_UNTIL_EXPIRATION);
    }
}
