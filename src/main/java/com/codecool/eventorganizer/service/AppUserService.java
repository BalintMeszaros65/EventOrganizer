package com.codecool.eventorganizer.service;

import com.codecool.eventorganizer.model.AppUser;
import com.codecool.eventorganizer.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public void saveAndUpdateUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    public AppUser getUser(UUID id) {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
        if (optionalAppUser.isPresent()) {
            return optionalAppUser.get();
        } else {
            throw new NoSuchElementException("User not found by given id.");
        }
    }

    public void deleteUser(UUID id) {
        appUserRepository.deleteById(id);
    }
}
