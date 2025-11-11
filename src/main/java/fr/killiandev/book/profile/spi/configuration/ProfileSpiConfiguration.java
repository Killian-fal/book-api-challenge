package fr.killiandev.book.profile.spi.configuration;

import fr.killiandev.book.auth.spi.AuthProfileServiceSpi;
import fr.killiandev.book.guide.spi.GuideProfileServiceSpi;
import fr.killiandev.book.profile.domain.ProfileService;
import fr.killiandev.book.profile.spi.impl.AuthProfileServiceSpiImpl;
import fr.killiandev.book.profile.spi.impl.GuideProfileServiceSpiImpl;
import fr.killiandev.book.profile.spi.impl.QuestionProfileServiceSpiImpl;
import fr.killiandev.book.profile.spi.impl.SupabaseProfileServiceSpiImpl;
import fr.killiandev.book.question.spi.QuestionProfileServiceSpi;
import fr.killiandev.book.supabase.spi.SupabaseProfileServiceSpi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileSpiConfiguration {

    @Bean
    public AuthProfileServiceSpi profileServiceSpi(ProfileService profileService) {
        return new AuthProfileServiceSpiImpl(profileService);
    }

    @Bean
    public GuideProfileServiceSpi guideProfileServiceSpi(ProfileService profileService) {
        return new GuideProfileServiceSpiImpl(profileService);
    }

    @Bean
    public QuestionProfileServiceSpi questionProfileServiceSpi(ProfileService profileService) {
        return new QuestionProfileServiceSpiImpl(profileService);
    }

    @Bean
    public SupabaseProfileServiceSpi supabaseProfileServiceSpi(ProfileService profileService) {
        return new SupabaseProfileServiceSpiImpl(profileService);
    }
}
