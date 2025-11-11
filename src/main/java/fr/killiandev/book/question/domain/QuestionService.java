package fr.killiandev.book.question.domain;

import fr.killiandev.book.question.domain.dto.ListQuestionDto;
import jakarta.validation.constraints.NotNull;

public interface QuestionService {

    @NotNull
    ListQuestionDto getActiveQuestions();

    @NotNull
    ListQuestionDto getPendingQuestions(@NotNull String phoneNumber);

    void submitAnswer(@NotNull String phoneNumber, @NotNull Long questionId, @NotNull String answer);
}
