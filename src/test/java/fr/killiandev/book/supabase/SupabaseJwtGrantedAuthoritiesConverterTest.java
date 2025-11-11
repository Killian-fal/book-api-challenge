package fr.killiandev.book.supabase;

import static fr.killiandev.book.observability.api.exception.ExceptionType.SUPABASE_INVALID_JWT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import fr.killiandev.book.observability.api.exception.type.SupabaseException;
import fr.killiandev.book.supabase.domain.converter.SupabaseJwtGrantedAuthoritiesConverter;
import fr.killiandev.book.supabase.spi.SupabaseProfileServiceSpi;
import fr.killiandev.book.supabase.spi.dto.SupabaseProfileDto;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

class SupabaseJwtGrantedAuthoritiesConverterTest {

    private SupabaseProfileServiceSpi profileServiceSpi;
    private SupabaseJwtGrantedAuthoritiesConverter converter;

    @BeforeEach
    void setUp() {
        profileServiceSpi = Mockito.mock(SupabaseProfileServiceSpi.class);
        converter = new SupabaseJwtGrantedAuthoritiesConverter(profileServiceSpi);
    }

    @Test
    void convertsPhoneClaimToRole() {
        SupabaseProfileDto profile = new SupabaseProfileDto(1L, SupabaseProfileDto.RoleType.ADMIN);
        when(profileServiceSpi.getProfile("+33600000000")).thenReturn(profile);

        Jwt jwt = Jwt.withTokenValue("token")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60))
                .header("alg", "HS256")
                .claim("phone", "+33600000000")
                .build();

        Collection<GrantedAuthority> authorities = converter.convert(jwt);

        assertThat(authorities)
                .singleElement()
                .extracting(GrantedAuthority::getAuthority)
                .isEqualTo("ROLE_ADMIN");
    }

    @Test
    void missingPhoneClaimThrowsSupabaseException() {
        Jwt jwt = new Jwt(
                "token",
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("alg", "HS256"),
                Map.of("email", "test@test.fr"));

        assertThatThrownBy(() -> converter.convert(jwt))
                .isInstanceOf(SupabaseException.class)
                .satisfies(ex ->
                        assertThat(((SupabaseException) ex).getExceptionType()).isEqualTo(SUPABASE_INVALID_JWT));
    }
}
