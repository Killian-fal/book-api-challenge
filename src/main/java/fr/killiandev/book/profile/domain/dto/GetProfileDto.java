package fr.killiandev.book.profile.domain.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileDto {

    @NotNull
    private Long id;

    @NotNull
    private String phoneNumber;

    @Nullable
    private String email;

    @Nullable
    private String fullName;

    @Nullable
    private String profilePicture;

    @NotNull
    private String role;
}
