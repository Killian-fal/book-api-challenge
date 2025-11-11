package fr.killiandev.book.auth.spi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponseDto {

    @NotNull
    private String accessToken;

    @NotNull
    private String tokenType;

    private int expiresIn;

    private long expiresAt;

    @NotNull
    private String refreshToken;

    @NotNull
    private User user;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {

        @NotNull
        private String email;

        @NotNull
        private String phone;
    }
}
