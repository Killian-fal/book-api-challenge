package fr.killiandev.book.profile.spi.impl;

import fr.killiandev.book.profile.domain.ProfileService;
import fr.killiandev.book.profile.spi.mapper.ProfileSpiMapper;
import fr.killiandev.book.supabase.spi.SupabaseProfileServiceSpi;
import fr.killiandev.book.supabase.spi.dto.SupabaseProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class SupabaseProfileServiceSpiImpl implements SupabaseProfileServiceSpi {

    private final ProfileService profileService;

    @Override
    public SupabaseProfileDto getProfile(String phoneNumber) {
        return ProfileSpiMapper.INSTANCE.ofSupabase(profileService.getProfile(phoneNumber));
    }
}
