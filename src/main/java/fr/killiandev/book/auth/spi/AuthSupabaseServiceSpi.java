package fr.killiandev.book.auth.spi;

import fr.killiandev.book.auth.spi.dto.AccessTokenResponseDto;
import fr.killiandev.book.observability.api.exception.type.SupabaseException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatusCode;

public interface AuthSupabaseServiceSpi {

    HttpStatusCode otp(@NotNull String phoneNumber) throws SupabaseException;

    @NotNull
    AccessTokenResponseDto verifyOtp(@NotNull String phoneNumber, @NotNull String code) throws SupabaseException;

    @NotNull
    AccessTokenResponseDto refresh(@NotNull String refreshToken) throws SupabaseException;
}
