package fr.killiandev.book.supabase.spi.configuration;

import fr.killiandev.book.auth.spi.AuthSupabaseServiceSpi;
import fr.killiandev.book.supabase.domain.SupabaseService;
import fr.killiandev.book.supabase.spi.impl.AuthSupabaseServiceSpiImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseSpiConfiguration {

    @Bean
    public AuthSupabaseServiceSpi supabaseServiceSpi(SupabaseService supabaseService) {
        return new AuthSupabaseServiceSpiImpl(supabaseService);
    }
}
