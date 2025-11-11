package fr.killiandev.book.auth.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerifyResponseDto {

    @NotNull
    private String registerCode;
}
