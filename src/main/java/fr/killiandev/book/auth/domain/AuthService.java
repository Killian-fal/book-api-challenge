package fr.killiandev.book.auth.domain;

import fr.killiandev.book.auth.domain.dto.AuthTokenResponseDto;
import fr.killiandev.book.auth.domain.dto.OTPCodeRequestDto;
import fr.killiandev.book.auth.domain.dto.RefreshRequestDto;
import fr.killiandev.book.auth.domain.dto.VerifyRequestDto;
import jakarta.validation.constraints.NotNull;

public interface AuthService {

    void sendOTPCode(@NotNull OTPCodeRequestDto request);

    @NotNull
    AuthTokenResponseDto login(@NotNull VerifyRequestDto verifyRequest);

    @NotNull
    AuthTokenResponseDto refresh(@NotNull RefreshRequestDto refreshRequest);
}
