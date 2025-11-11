package fr.killiandev.book.auth.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenResponseDto {

    @NotNull
    private String accessToken;

    @NotNull
    private String tokenType;

    private int expiresIn;

    private long expiresAt;

    @NotNull
    private String refreshToken;
}
