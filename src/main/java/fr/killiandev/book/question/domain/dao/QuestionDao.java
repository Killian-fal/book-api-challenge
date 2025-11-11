package fr.killiandev.book.question.domain.dao;

import fr.killiandev.book.common.Dao;
import fr.killiandev.book.question.domain.entity.Question;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface QuestionDao extends Dao<Question, Long> {

    @NotNull
    List<Question> findActiveQuestions();

    @NotNull
    List<Question> findPendingQuestions(@NotNull Long profileId);
}
