package fr.killiandev.book.profile.spi.impl;

import fr.killiandev.book.guide.spi.GuideProfileServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideProfileDto;
import fr.killiandev.book.profile.domain.ProfileService;
import fr.killiandev.book.profile.spi.mapper.ProfileSpiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GuideProfileServiceSpiImpl implements GuideProfileServiceSpi {

    private final ProfileService profileService;

    @Override
    public GuideProfileDto getProfile(String phoneNumber) {
        return ProfileSpiMapper.INSTANCE.ofGuide(profileService.getProfile(phoneNumber));
    }
}
