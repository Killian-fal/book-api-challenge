package fr.killiandev.book.profile.spi.impl;

import fr.killiandev.book.profile.domain.ProfileService;
import fr.killiandev.book.question.spi.QuestionProfileServiceSpi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class QuestionProfileServiceSpiImpl implements QuestionProfileServiceSpi {

    private final ProfileService profileService;

    @Override
    public long getProfileId(String phoneNumber) {
        return profileService.getProfile(phoneNumber).getId();
    }
}
