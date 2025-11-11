package fr.killiandev.book.auth.domain.configuration;

import fr.killiandev.book.auth.domain.AuthService;
import fr.killiandev.book.auth.domain.AuthServiceImpl;
import fr.killiandev.book.auth.spi.AuthProfileServiceSpi;
import fr.killiandev.book.auth.spi.AuthSupabaseServiceSpi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {

    @Bean
    public AuthService authService(
            AuthSupabaseServiceSpi supabaseService, AuthProfileServiceSpi authProfileServiceSpi) {
        return new AuthServiceImpl(supabaseService, authProfileServiceSpi);
    }
}
