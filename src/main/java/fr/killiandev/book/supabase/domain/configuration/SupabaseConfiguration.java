package fr.killiandev.book.supabase.domain.configuration;

import fr.killiandev.book.supabase.domain.SupabaseService;
import fr.killiandev.book.supabase.domain.SupabaseServiceImpl;
import fr.killiandev.book.supabase.domain.configuration.properties.SupabaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(SupabaseProperties.class)
@Configuration
public class SupabaseConfiguration {

    @Bean
    public SupabaseService supabaseService(SupabaseProperties properties, RestTemplate restTemplateSupabase) {
        return new SupabaseServiceImpl(properties, restTemplateSupabase);
    }

    @Bean
    public RestTemplate restTemplateSupabase(RestTemplateBuilder builder) {
        return builder.build();
    }
}
