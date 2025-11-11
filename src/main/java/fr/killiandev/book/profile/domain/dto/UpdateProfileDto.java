package fr.killiandev.book.profile.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDto {

    //    TODO: WITH OTP (sendgrid)
    //    @NotEmpty
    //    private String email;

    @NotEmpty
    private String fullName;
}
