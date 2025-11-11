package fr.killiandev.book.question.domain.dao;

import fr.killiandev.book.common.Dao;
import fr.killiandev.book.question.domain.entity.ProfileAnswer;
import jakarta.validation.constraints.NotNull;

public interface ProfileAnswerDao extends Dao<ProfileAnswer, Long> {

    boolean existsByProfileAndQuestion(@NotNull Long profileId, @NotNull Long questionId);
}
