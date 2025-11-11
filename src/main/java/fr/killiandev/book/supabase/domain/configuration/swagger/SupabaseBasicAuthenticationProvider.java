package fr.killiandev.book.supabase.domain.configuration.swagger;

import fr.killiandev.book.supabase.domain.SupabaseService;
import fr.killiandev.book.supabase.domain.dto.SupabaseAccessTokenResponseDto;
import fr.killiandev.book.supabase.spi.SupabaseProfileServiceSpi;
import fr.killiandev.book.supabase.spi.dto.SupabaseProfileDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public class SupabaseBasicAuthenticationProvider implements AuthenticationProvider {

    private final SupabaseService supabaseService;
    private final SupabaseProfileServiceSpi profileServiceSpi;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = authentication.getName();
        String password = (String) authentication.getCredentials();

        SupabaseAccessTokenResponseDto response = supabaseService.authenticate(phoneNumber, password);

        SupabaseProfileDto profileDto = profileServiceSpi.getProfile(phoneNumber);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                phoneNumber,
                null,
                List.of(new SimpleGrantedAuthority(profileDto.getRole().asAuthority())));
        authToken.setDetails(response.getAccessToken());

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
