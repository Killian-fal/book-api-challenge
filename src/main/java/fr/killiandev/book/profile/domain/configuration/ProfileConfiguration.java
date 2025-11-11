package fr.killiandev.book.profile.domain.configuration;

import fr.killiandev.book.profile.domain.ProfileService;
import fr.killiandev.book.profile.domain.ProfileServiceImpl;
import fr.killiandev.book.profile.domain.dao.ProfileDao;
import fr.killiandev.book.profile.spi.ProfileStorageServiceSpi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileConfiguration {

    @Bean
    public ProfileService profileService(ProfileDao profileDao, ProfileStorageServiceSpi profileStorageServiceSpi) {
        return new ProfileServiceImpl(profileDao, profileStorageServiceSpi);
    }
}
