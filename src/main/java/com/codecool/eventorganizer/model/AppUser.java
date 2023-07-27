package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.utility.BasicInfoValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @Email(groups = BasicInfoValidation.class)
    @Column(unique = true)
    @Schema(example = "testemail@gmail.com")
    private String email;
    @NotBlank(groups = BasicInfoValidation.class)
    @Schema(example = "testpw")
    private String password;
    @NotBlank(groups = BasicInfoValidation.class)
    @Schema(example = "John")
    private String firstName;
    @NotBlank(groups = BasicInfoValidation.class)
    @Schema(example = "Smith")
    private String lastName;
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    List<String> roles;
    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser appUser)) return false;
        return isEnabled() == appUser.isEnabled() && Objects.equals(getId(), appUser.getId())
                && Objects.equals(getEmail(), appUser.getEmail()) && Objects.equals(getPassword(), appUser.getPassword())
                && Objects.equals(getFirstName(), appUser.getFirstName())
                && Objects.equals(getLastName(), appUser.getLastName()) && Objects.equals(getRoles(), appUser.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword(), getFirstName(), getLastName(), getRoles(), isEnabled());
    }
}
