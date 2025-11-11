package fr.killiandev.book.guide.domain.dto;

import fr.killiandev.book.guide.domain.validator.ISO639_1;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranslateGuideDto {

    @NotNull
    private Long guideId;

    @ISO639_1
    @NotEmpty
    private String language;
}
