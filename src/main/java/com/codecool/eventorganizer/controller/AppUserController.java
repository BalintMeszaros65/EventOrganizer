package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.service.AppUserService;
import com.codecool.eventorganizer.utility.BasicInfoValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(
    name = "User",
    description = "Operations about user"
)
@Validated
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/api/user/register")
    @Operation(summary = "registers a simple user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "created entity",
            content = @Content(
                schema = @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZW1haWxAZ21haWwuY29tIiwiaWF0IjoxNjgwNzA0MTQxLCJleHAiOjE2ODA3NDAxNDF9.GfkiY5M_-1F--oJWJGUxBa80E7L_EY3S_ZXH1LhuAZM")
            )),
        @ApiResponse(
            responseCode = "400",
            description = "something went wrong"
        )
    })
    public ResponseEntity<String> registerUser(
            @Validated(BasicInfoValidation.class)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "AppUser Object where id (generated), roles (generated) must be null."
            )
            @RequestBody AppUser appUser) {
        return appUserService.registerCustomer(appUser);
    }

    @PostMapping("/api/organizer/register/{secret_key}")
    @Operation(summary = "registers an organizer")
    public ResponseEntity<String> registerOrganizer(
            @Validated(BasicInfoValidation.class)
            @RequestBody AppUser appUser,
            @Parameter(description = "Secret key for organizer registration", required = true)
            @PathVariable(name = "secret_key") String secretKey) {
        return appUserService.registerOrganizer(appUser, secretKey);
    }

    @PostMapping("/api/admin/register/{secret_key}")
    @Operation(summary = "registers an admin")
    public ResponseEntity<String> registerAdmin(
            @Validated(BasicInfoValidation.class)
            @RequestBody AppUser appUser,
            @Parameter(description = "Secret key for admin registration", required = true)
            @PathVariable(name = "secret_key") String secretKey) {
        return appUserService.registerAdmin(appUser, secretKey);
    }

    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @SecurityRequirement(name = "Basic Authentication")
    @PutMapping("/api/user/change-name")
    @Operation(summary = "updates the current user's information")
    public ResponseEntity<String> changeUserName(
            @Validated(BasicInfoValidation.class)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description =
                    "AppUser Object where id, email and password must not differ from the ones stored in database."
            )
            @RequestBody AppUser appUser) {
        return appUserService.updateUserInformation(appUser);
    }

    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @SecurityRequirement(name = "Basic Authentication")
    @PutMapping("/api/user/change-password")
    @Operation(summary = "changes the current user's password")
    public ResponseEntity<String> changeUserPassword(
            @NotBlank
            @Parameter(description = "New password of user", required = true)
            @RequestParam String newPassword) {
        return appUserService.changePassword(newPassword);
    }

    @Secured({"ROLE_USER", "ROLE_ORGANIZER", "ROLE_ADMIN"})
    @SecurityRequirement(name = "Bearer Authentication")
    @SecurityRequirement(name = "Basic Authentication")
    @DeleteMapping("/api/user/delete")
    @Operation(summary = "deletes the current user")
    public ResponseEntity<String> deleteUser() {
        return appUserService.deleteUser();
    }
}
