package fr.killiandev.book.question.domain.repository;

import fr.killiandev.book.question.domain.entity.ProfileAnswer;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;

public interface ProfileAnswerRepository extends CrudRepository<ProfileAnswer, Long> {

    boolean existsByProfileIdAndQuestion_Id(@NotNull Long profileId, @NotNull Long questionId);
}
