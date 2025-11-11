package fr.killiandev.book.supabase.domain;

import fr.killiandev.book.observability.api.exception.type.SupabaseException;
import fr.killiandev.book.supabase.domain.dto.SupabaseAccessTokenResponseDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatusCode;

public interface SupabaseService {

    HttpStatusCode otp(@NotNull String phoneNumber) throws SupabaseException;

    @NotNull
    SupabaseAccessTokenResponseDto verifyOtp(@NotNull String phoneNumber, @NotNull String code)
            throws SupabaseException;

    @NotNull
    SupabaseAccessTokenResponseDto authenticate(@NotNull String phoneNumber, @NotNull String password)
            throws SupabaseException;

    @NotNull
    SupabaseAccessTokenResponseDto refresh(@NotNull String refreshToken) throws SupabaseException;
}
