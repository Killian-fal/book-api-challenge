package fr.killiandev.book.supabase.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupabaseAccessTokenResponseDto {

    @NotNull
    @JsonProperty("access_token")
    private String accessToken;

    @NotNull
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("expires_at")
    private long expiresAt;

    @NotNull
    @JsonProperty("refresh_token")
    private String refreshToken;

    @NotNull
    @JsonProperty("user")
    private User user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {

        @NotNull
        private String id;

        @NotNull
        private String aud;

        @NotNull
        private String role;

        @NotNull
        private String email;

        @NotNull
        private String phone;

        @NotNull
        @JsonProperty("phone_confirmed_at")
        private String phoneConfirmedAt;

        @NotNull
        @JsonProperty("confirmation_sent_at")
        private String confirmationSentAt;

        @NotNull
        @JsonProperty("confirmed_at")
        private String confirmedAt;

        @NotNull
        @JsonProperty("last_sign_in_at")
        private String lastSignInAt;

        @NotNull
        @JsonProperty("app_metadata")
        private AppMetadata appMetadata;

        @NotNull
        @JsonProperty("user_metadata")
        private UserMetadata userMetadata;

        @NotNull
        private List<Identity> identities;

        @NotNull
        @JsonProperty("created_at")
        private String createdAt;

        @NotNull
        @JsonProperty("updated_at")
        private String updatedAt;

        @JsonProperty("is_anonymous")
        private boolean isAnonymous;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppMetadata {

        @NotNull
        private String provider;

        @NotNull
        private List<String> providers;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserMetadata {

        @JsonProperty("email_verified")
        private boolean emailVerified;

        @JsonProperty("phone_verified")
        private boolean phoneVerified;

        @NotNull
        private String sub;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Identity {

        @NotNull
        @JsonProperty("identity_id")
        private String identityId;

        @NotNull
        private String id;

        @NotNull
        @JsonProperty("user_id")
        private String userId;

        @NotNull
        @JsonProperty("identity_data")
        private IdentityData identityData;

        @NotNull
        private String provider;

        @NotNull
        @JsonProperty("last_sign_in_at")
        private String lastSignInAt;

        @NotNull
        @JsonProperty("created_at")
        private String createdAt;

        @NotNull
        @JsonProperty("updated_at")
        private String updatedAt;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdentityData {

        @JsonProperty("email_verified")
        private boolean emailVerified;

        @JsonProperty("phone_verified")
        private boolean phoneVerified;

        @NotNull
        private String sub;
    }
}
