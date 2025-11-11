package fr.killiandev.book.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.killiandev.book.auth.domain.AuthService;
import fr.killiandev.book.auth.domain.configuration.AuthConfiguration;
import fr.killiandev.book.auth.domain.dto.AuthTokenResponseDto;
import fr.killiandev.book.auth.domain.dto.VerifyRequestDto;
import fr.killiandev.book.auth.spi.AuthProfileServiceSpi;
import fr.killiandev.book.auth.spi.AuthSupabaseServiceSpi;
import fr.killiandev.book.auth.spi.dto.AccessTokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(AuthConfiguration.class)
class AuthServiceImplTest {

    private static final String PHONE_NUMBER = "+33600000000";

    @MockitoBean
    private AuthSupabaseServiceSpi authSupabaseServiceSpi;

    @MockitoBean
    private AuthProfileServiceSpi authProfileServiceSpi;

    @Autowired
    private AuthService authService;

    @Test
    void loginReturnsTokensWhenProfileExists() {
        VerifyRequestDto request = new VerifyRequestDto(PHONE_NUMBER, "123456");
        AccessTokenResponseDto supabaseResponse = supabaseResponse();

        when(authSupabaseServiceSpi.verifyOtp(PHONE_NUMBER, "123456")).thenReturn(supabaseResponse);
        when(authProfileServiceSpi.profileExists(PHONE_NUMBER)).thenReturn(true);

        AuthTokenResponseDto response = authService.login(request);

        assertThat(response.getAccessToken()).isEqualTo(supabaseResponse.getAccessToken());
        assertThat(response.getRefreshToken()).isEqualTo(supabaseResponse.getRefreshToken());
        assertThat(response.getTokenType()).isEqualTo(supabaseResponse.getTokenType());
        assertThat(response.getExpiresIn()).isEqualTo(supabaseResponse.getExpiresIn());
        assertThat(response.getExpiresAt()).isEqualTo(supabaseResponse.getExpiresAt());

        verify(authProfileServiceSpi, never()).createProfile(PHONE_NUMBER);
    }

    @Test
    void loginCreatesProfileWhenMissing() {
        VerifyRequestDto request = new VerifyRequestDto(PHONE_NUMBER, "654321");
        AccessTokenResponseDto supabaseResponse = supabaseResponse();

        when(authSupabaseServiceSpi.verifyOtp(PHONE_NUMBER, "654321")).thenReturn(supabaseResponse);
        when(authProfileServiceSpi.profileExists(PHONE_NUMBER)).thenReturn(false);
        when(authProfileServiceSpi.createProfile(PHONE_NUMBER)).thenReturn(42L);

        AuthTokenResponseDto response = authService.login(request);

        assertThat(response.getAccessToken()).isEqualTo(supabaseResponse.getAccessToken());
        verify(authProfileServiceSpi).createProfile(PHONE_NUMBER);
    }

    private static AccessTokenResponseDto supabaseResponse() {
        AccessTokenResponseDto.User user = new AccessTokenResponseDto.User("email@test.fr", PHONE_NUMBER);
        return new AccessTokenResponseDto("access", "Bearer", 3600, 1700000000L, "refresh", user);
    }
}
