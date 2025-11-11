package fr.killiandev.book.question.domain.dto;

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
public class SubmitProfileAnswerRequestDto {

    @NotNull
    private Long questionId;

    @NotEmpty
    private String answer;
}
