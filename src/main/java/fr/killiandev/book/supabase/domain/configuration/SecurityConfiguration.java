package fr.killiandev.book.supabase.domain.configuration;

import fr.killiandev.book.supabase.domain.SupabaseService;
import fr.killiandev.book.supabase.domain.configuration.properties.SupabaseProperties;
import fr.killiandev.book.supabase.domain.configuration.swagger.SupabaseBasicAuthenticationProvider;
import fr.killiandev.book.supabase.domain.converter.SupabaseJwtGrantedAuthoritiesConverter;
import fr.killiandev.book.supabase.domain.filter.RateLimitFilter;
import fr.killiandev.book.supabase.spi.SupabaseProfileServiceSpi;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableMethodSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public JwtDecoder jwtDecoder(SupabaseProperties properties) {
        SecretKey key = new SecretKeySpec(properties.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SupabaseService supabaseService,
            SupabaseProfileServiceSpi profileServiceSpi,
            JwtAuthenticationConverter jwtAuthenticationConverter)
            throws Exception {
        // TODO: activate cors and csrf
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/v1/auth/send",
                                "/api/v1/auth/verify",
                                "/api/v1/auth/refresh",
                                "/api/v1/guide/public/**")
                        .permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        // TODO: a way to disable this when springdoc.swagger-ui.enabled is false
        // With: .securityMatcher("/swagger-ui/**", "/v3/api-docs/**")
        http.httpBasic(Customizer.withDefaults())
                .authenticationProvider(new SupabaseBasicAuthenticationProvider(supabaseService, profileServiceSpi));

        http.addFilterBefore(new RateLimitFilter(), LogoutFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(
            SupabaseJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setPrincipalClaimName("phone");
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    public SupabaseJwtGrantedAuthoritiesConverter supabaseJwtGrantedAuthoritiesConverter(
            SupabaseProfileServiceSpi profileServiceSpi) {
        return new SupabaseJwtGrantedAuthoritiesConverter(profileServiceSpi);
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
