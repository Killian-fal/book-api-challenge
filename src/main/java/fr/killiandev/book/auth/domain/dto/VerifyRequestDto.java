package fr.killiandev.book.auth.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyRequestDto {

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private String code;
}
