package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/api/user/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser appUser) {
        return appUserService.registerUser(appUser);
    }

    @PostMapping("/api/organizer/register/{secret_key}")
    public ResponseEntity<String> registerOrganizer(@RequestBody AppUser appUser,
                                                    @PathVariable(name = "secret_key") String secretKey) {
        return appUserService.registerOrganizer(appUser, secretKey);
    }

    @PostMapping("/api/admin/register/{secret_key}")
    public ResponseEntity<String> registerAdmin(@RequestBody AppUser appUser,
                                                @PathVariable(name = "secret_key") String secretKey) {
        return appUserService.registerAdmin(appUser, secretKey);
    }

    @PutMapping("/api/user/update-information")
    public ResponseEntity<String> updateUser(@RequestBody AppUser appUser) {
        return appUserService.updateUserInformation(appUser);
    }

    @PutMapping("/api/user/change-password")
    public ResponseEntity<String> changePasswordOfUser(@RequestParam String newPassword) {
        return appUserService.changePassword(newPassword);
    }

    @DeleteMapping("/api/user/delete")
    public ResponseEntity<String> deleteUser() {
        return appUserService.deleteUser();
    }
}
