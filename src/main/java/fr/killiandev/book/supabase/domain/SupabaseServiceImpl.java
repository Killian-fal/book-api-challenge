package fr.killiandev.book.supabase.domain;

import static fr.killiandev.book.observability.api.exception.ExceptionType.SUPABASE_RATE_LIMIT_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.SUPABASE_UNKNOW_ERROR;
import static fr.killiandev.book.supabase.domain.SupabaseConstant.*;

import fr.killiandev.book.observability.api.exception.type.SupabaseException;
import fr.killiandev.book.supabase.domain.configuration.properties.SupabaseProperties;
import fr.killiandev.book.supabase.domain.dto.SupabaseAccessTokenResponseDto;
import fr.killiandev.book.supabase.domain.dto.SupabaseVerifyRequest;
import java.util.Map;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public class SupabaseServiceImpl implements SupabaseService {

    private static final String DEFAULT_SUPABASE_TYPE = "sms";

    private final SupabaseProperties supabaseProperties;
    private final RestTemplate restTemplate;

    @Override
    public HttpStatusCode otp(String phoneNumber) throws SupabaseException {
        log.info("- Sending OTP to the phone number: {}", phoneNumber);

        HttpEntity<Map<String, String>> requestEntity = buildRequestEntity(Map.of("phone", phoneNumber));

        return handleException(
                () -> {
                    ResponseEntity<String> response = restTemplate.postForEntity(
                            SUPABASE_OTP_URL.formatted(supabaseProperties.getUrl()), requestEntity, String.class);

                    if (response.getStatusCode() != HttpStatus.OK) {
                        throw new SupabaseException(
                                SUPABASE_UNKNOW_ERROR, "Failed to send OTP", "phoneNumber", phoneNumber);
                    }
                    return response.getStatusCode();
                },
                "phoneNumber",
                phoneNumber);
    }

    @Override
    public SupabaseAccessTokenResponseDto verifyOtp(String phoneNumber, String code) throws SupabaseException {
        log.info("- Verifying OTP for the phone number: {}", phoneNumber);

        SupabaseVerifyRequest request = SupabaseVerifyRequest.builder()
                .phone(phoneNumber)
                .token(code)
                .type(DEFAULT_SUPABASE_TYPE)
                .build();
        HttpEntity<SupabaseVerifyRequest> requestEntity = buildRequestEntity(request);

        return handleException(
                () -> {
                    ResponseEntity<SupabaseAccessTokenResponseDto> response = restTemplate.postForEntity(
                            SUPABASE_VERIFY_OTP_URL.formatted(supabaseProperties.getUrl()),
                            requestEntity,
                            SupabaseAccessTokenResponseDto.class);

                    log.info("OTP verified successfully for the phone number: {}", phoneNumber);
                    return response.getBody();
                },
                "phoneNumber",
                phoneNumber);
    }

    @Override
    public SupabaseAccessTokenResponseDto authenticate(String phoneNumber, String password) throws SupabaseException {
        log.info("- Authenticating the phone number: {}", phoneNumber);
        Map<String, String> request = Map.of("phone", phoneNumber, "password", password);
        HttpEntity<Map<String, String>> requestEntity = buildRequestEntity(request);

        return handleException(
                () -> {
                    ResponseEntity<SupabaseAccessTokenResponseDto> response = restTemplate.postForEntity(
                            SUPABASE_AUTHENTICATE.formatted(supabaseProperties.getUrl()),
                            requestEntity,
                            SupabaseAccessTokenResponseDto.class);

                    return response.getBody();
                },
                "phoneNumber",
                phoneNumber);
    }

    @Override
    public SupabaseAccessTokenResponseDto refresh(String refreshToken) throws SupabaseException {
        Map<String, String> request = Map.of("refresh_token", refreshToken);
        HttpEntity<Map<String, String>> requestEntity = buildRequestEntity(request);

        return handleException(() -> {
            ResponseEntity<SupabaseAccessTokenResponseDto> response = restTemplate.postForEntity(
                    SUPABASE_REFRESH.formatted(supabaseProperties.getUrl()),
                    requestEntity,
                    SupabaseAccessTokenResponseDto.class);

            return response.getBody();
        });
    }

    private <T> HttpEntity<T> buildRequestEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apikey", supabaseProperties.getKey());
        return new HttpEntity<>(body, headers);
    }

    private <T> T handleException(Supplier<T> supplier, Object... args) throws SupabaseException {
        try {
            return supplier.get();
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(429))) {
                throw new SupabaseException(SUPABASE_RATE_LIMIT_ERROR, exception, args);
            }
            throw new SupabaseException(SUPABASE_UNKNOW_ERROR, exception, args);
        } catch (Exception exception) {
            throw new SupabaseException(SUPABASE_UNKNOW_ERROR, exception, args);
        }
    }
}
