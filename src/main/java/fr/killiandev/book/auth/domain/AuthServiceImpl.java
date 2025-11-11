package fr.killiandev.book.auth.domain;

import fr.killiandev.book.auth.domain.dto.AuthTokenResponseDto;
import fr.killiandev.book.auth.domain.dto.OTPCodeRequestDto;
import fr.killiandev.book.auth.domain.dto.RefreshRequestDto;
import fr.killiandev.book.auth.domain.dto.VerifyRequestDto;
import fr.killiandev.book.auth.domain.mapper.AuthMapper;
import fr.killiandev.book.auth.spi.AuthProfileServiceSpi;
import fr.killiandev.book.auth.spi.AuthSupabaseServiceSpi;
import fr.killiandev.book.auth.spi.dto.AccessTokenResponseDto;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthSupabaseServiceSpi supabaseServiceSpi;
    private final AuthProfileServiceSpi authProfileServiceSpi;

    @NewSpan("AuthService - sendOTPCode")
    @Override
    public void sendOTPCode(OTPCodeRequestDto request) {
        log.info("- Send SMS verify for registration to the user with phone number: {}", request.getPhoneNumber());

        supabaseServiceSpi.otp(request.getPhoneNumber());
    }

    @NewSpan("AuthService - login")
    @Transactional
    @Override
    public AuthTokenResponseDto login(VerifyRequestDto verifyRequest) {
        log.info("- Login the user with phone number: {}", verifyRequest.getPhoneNumber());

        AccessTokenResponseDto response =
                supabaseServiceSpi.verifyOtp(verifyRequest.getPhoneNumber(), verifyRequest.getCode());

        String phoneNumber = response.getUser().getPhone();

        if (authProfileServiceSpi.profileExists(phoneNumber)) {
            return AuthMapper.INSTANCE.of(response);
        }

        log.info("- Profile not found, creating a new one..");
        long profileId = authProfileServiceSpi.createProfile(phoneNumber);
        log.info("- Profile created: {}", profileId);

        return AuthMapper.INSTANCE.of(response);
    }

    @NewSpan("AuthService - refresh")
    @Override
    public AuthTokenResponseDto refresh(RefreshRequestDto refreshRequest) {
        log.info("- Refresh the token of an user");

        AccessTokenResponseDto response = supabaseServiceSpi.refresh(refreshRequest.getRefreshToken());

        return AuthMapper.INSTANCE.of(response);
    }
}
