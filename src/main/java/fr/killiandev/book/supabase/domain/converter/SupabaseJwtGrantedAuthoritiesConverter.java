package fr.killiandev.book.supabase.domain.converter;

import static fr.killiandev.book.observability.api.exception.ExceptionType.SUPABASE_INVALID_JWT;

import fr.killiandev.book.observability.api.exception.type.SupabaseException;
import fr.killiandev.book.supabase.spi.SupabaseProfileServiceSpi;
import fr.killiandev.book.supabase.spi.dto.SupabaseProfileDto;
import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class SupabaseJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final SupabaseProfileServiceSpi profileServiceSpi;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        String phoneNumber = jwt.getClaimAsString("phone");

        if (!StringUtils.hasText(phoneNumber)) {
            throw new SupabaseException(SUPABASE_INVALID_JWT);
        }

        SupabaseProfileDto profileDto = profileServiceSpi.getProfile(phoneNumber);

        return Set.of(new SimpleGrantedAuthority(profileDto.getRole().asAuthority()));
    }
}
