package com.codecool.eventorganizer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @Column(unique = true)
    @Schema(example = "testemail@gmail.com")
    private String email;
    @NotNull
    @Schema(example = "testpw")
    private String password;
    @NotNull
    @Schema(example = "John")
    private String firstName;
    @NotNull
    @Schema(example = "Smith")
    private String lastName;
    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    List<String> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser appUser)) return false;
        return Objects.equals(id, appUser.id) && Objects.equals(email, appUser.email)
                && Objects.equals(password, appUser.password) && Objects.equals(firstName, appUser.firstName)
                && Objects.equals(lastName, appUser.lastName) && Objects.equals(roles, appUser.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName, roles);
    }
}
