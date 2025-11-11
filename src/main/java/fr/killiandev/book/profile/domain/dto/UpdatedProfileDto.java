package fr.killiandev.book.profile.domain.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedProfileDto {

    //    TODO: WITH OTP (sendgrid)
    //    @Nullable
    //    private String email;

    @Nullable
    private String fullName;
}
