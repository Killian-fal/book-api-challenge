package fr.killiandev.book.auth.domain;

import fr.killiandev.book.auth.domain.dto.AuthTokenResponseDto;
import fr.killiandev.book.auth.domain.dto.OTPCodeRequestDto;
import fr.killiandev.book.auth.domain.dto.RefreshRequestDto;
import fr.killiandev.book.auth.domain.dto.VerifyRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Send an OTP code to the given phone number")
    @PostMapping("/send")
    public void send(@Valid @RequestBody OTPCodeRequestDto otpCodeRequest) {
        log.info("Received OTP code request for phone number: {}", otpCodeRequest.getPhoneNumber());
        authService.sendOTPCode(otpCodeRequest);
    }

    @Operation(summary = "Verify the OTP code and login or register the user")
    @PostMapping("/verify")
    public AuthTokenResponseDto login(@Valid @RequestBody VerifyRequestDto verifyRequest) {
        log.info("Received Verify request for phone number: {}", verifyRequest.getPhoneNumber());
        return authService.login(verifyRequest);
    }

    @Operation(summary = "Refresh the access token using the refresh token")
    @PostMapping("/refresh")
    public AuthTokenResponseDto refresh(@Valid @RequestBody RefreshRequestDto refreshRequest) {
        log.info("Received Refresh token request");
        return authService.refresh(refreshRequest);
    }
}
