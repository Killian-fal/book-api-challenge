package fr.killiandev.book.supabase;

import static fr.killiandev.book.observability.api.exception.ExceptionType.SUPABASE_RATE_LIMIT_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.SUPABASE_UNKNOW_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import fr.killiandev.book.observability.api.exception.type.SupabaseException;
import fr.killiandev.book.supabase.domain.SupabaseService;
import fr.killiandev.book.supabase.domain.configuration.SupabaseConfiguration;
import fr.killiandev.book.supabase.domain.dto.SupabaseAccessTokenResponseDto;
import fr.killiandev.book.supabase.support.RestTemplateBuilderTestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SpringJUnitConfig({SupabaseConfiguration.class, RestTemplateBuilderTestConfig.class})
@TestPropertySource(
        properties = {
            "supabase.url=" + SupabaseServiceIntegrationTest.BASE_URL,
            "supabase.key=key",
            "supabase.secret=secret"
        })
class SupabaseServiceIntegrationTest {

    static final String BASE_URL = "http://supabase.test";

    @Autowired
    private SupabaseService supabaseService;

    @Autowired
    private RestTemplate restTemplateSupabase;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restTemplateSupabase)
                .ignoreExpectOrder(true)
                .build();
    }

    @AfterEach
    void verifyServer() {
        mockServer.verify();
    }

    @Test
    void otpSuccess() {
        mockServer
                .expect(requestTo(BASE_URL + "/auth/v1/otp"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("apikey", "key"))
                .andExpect(content()
                        .json("""
                        {"phone":"+33600000000"}
                        """))
                .andRespond(withStatus(HttpStatus.OK).body("{}").contentType(MediaType.APPLICATION_JSON));

        HttpStatusCode status = supabaseService.otp("+33600000000");
        assertThat(status).isEqualTo(HttpStatus.OK);
    }

    @Test
    void otpFailure() {
        mockServer
                .expect(requestTo(BASE_URL + "/auth/v1/otp"))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> supabaseService.otp("+33600000001"))
                .isInstanceOf(SupabaseException.class)
                .satisfies(ex ->
                        assertThat(((SupabaseException) ex).getExceptionType()).isEqualTo(SUPABASE_UNKNOW_ERROR));
    }

    @Test
    void verifyOtpAndRefreshRateLimit() {
        mockServer
                .expect(requestTo(BASE_URL + "/auth/v1/token?grant_type=refresh_token"))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        assertThatThrownBy(() -> supabaseService.refresh("rate-limited"))
                .isInstanceOf(SupabaseException.class)
                .satisfies(ex ->
                        assertThat(((SupabaseException) ex).getExceptionType()).isEqualTo(SUPABASE_RATE_LIMIT_ERROR));
    }

    @Test
    void verifyOtpAndRefreshHandleMapping() {
        String responseBody =
                """
                {
                  "access_token": "token123",
                  "token_type": "bearer",
                  "expires_in": 900,
                  "expires_at": 123456,
                  "refresh_token": "refresh123",
                  "user": {
                    "id": "1",
                    "aud": "aud",
                    "role": "user",
                    "email": "john@example.com",
                    "phone": "+33600000000",
                    "phone_confirmed_at": "now",
                    "confirmation_sent_at": "now",
                    "confirmed_at": "now",
                    "last_sign_in_at": "now",
                    "app_metadata": { "provider": "phone", "providers": ["phone"] },
                    "user_metadata": { "email_verified": true, "phone_verified": true, "sub": "sub" },
                    "identities": [],
                    "created_at": "now",
                    "updated_at": "now",
                    "is_anonymous": false
                  }
                }
                """;

        mockServer
                .expect(requestTo(BASE_URL + "/auth/v1/verify"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseBody));

        mockServer
                .expect(requestTo(BASE_URL + "/auth/v1/token?grant_type=refresh_token"))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(responseBody));

        SupabaseAccessTokenResponseDto dto = supabaseService.verifyOtp("+33600000000", "123456");
        assertThat(dto.getAccessToken()).isEqualTo("token123");
        assertThat(dto.getRefreshToken()).isEqualTo("refresh123");
        assertThat(dto.getUser().getPhone()).isEqualTo("+33600000000");

        SupabaseAccessTokenResponseDto refreshed = supabaseService.refresh("refresh123");
        assertThat(refreshed.getUser().getEmail()).isEqualTo("john@example.com");
    }
}
