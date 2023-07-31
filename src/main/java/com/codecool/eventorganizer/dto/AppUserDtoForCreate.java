package com.codecool.eventorganizer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDtoForCreate {
    @Email
    @Schema(example = "testemail@gmail.com")
    private String email;
    @NotBlank
    @Schema(example = "testpw")
    private String password;
    @NotBlank
    @Schema(example = "John")
    private String firstName;
    @NotBlank
    @Schema(example = "Smith")
    private String lastName;
}
