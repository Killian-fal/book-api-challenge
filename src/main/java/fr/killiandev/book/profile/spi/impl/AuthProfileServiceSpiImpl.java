package fr.killiandev.book.profile.spi.impl;

import fr.killiandev.book.auth.spi.AuthProfileServiceSpi;
import fr.killiandev.book.profile.domain.ProfileService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthProfileServiceSpiImpl implements AuthProfileServiceSpi {

    private final ProfileService profileService;

    @Override
    public boolean profileExists(String phoneNumber) {
        return profileService.profileExists(phoneNumber);
    }

    @Override
    public long createProfile(String phoneNumber) {
        return profileService.createProfile(phoneNumber).getId();
    }
}
