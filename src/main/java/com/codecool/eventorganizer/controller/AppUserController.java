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

    // TODO logic to verify after security
    @PostMapping("/api/organizer/register")
    public ResponseEntity<String> registerOrganizer(@RequestBody AppUser appUser) {
        return appUserService.registerOrganizer(appUser);
    }

    // TODO logic to verify after security
    @PostMapping("/api/admin/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AppUser appUser) {
        return appUserService.registerAdmin(appUser);
    }

    @GetMapping("/api/user/login")
    public ResponseEntity<String> loginUser(@RequestBody AppUser appUser) {
        return appUserService.loginUser(appUser);
    }

    @PutMapping("/api/user/update-information")
    public ResponseEntity<String> updateUser(@RequestBody AppUser appUser) {
        return appUserService.updateUserInformation(appUser);
    }

    @PutMapping("/api/user/change-password")
    public ResponseEntity<String> changePasswordOfUser(@RequestParam String newPassword) {
        return appUserService.changePassword(newPassword);
    }
}
