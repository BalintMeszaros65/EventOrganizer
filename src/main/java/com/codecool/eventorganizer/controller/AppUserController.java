package com.codecool.eventorganizer.controller;

import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
