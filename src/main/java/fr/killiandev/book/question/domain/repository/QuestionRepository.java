package fr.killiandev.book.question.domain.repository;

import fr.killiandev.book.question.domain.entity.Question;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends CrudRepository<Question, Long> {

    @NotNull
    List<Question> findByActiveTrueOrderByDisplayOrder();

    @Query(
            "SELECT q FROM Question q LEFT JOIN ProfileAnswer a ON q.id = a.question.id AND a.profileId = :profileId WHERE q.active = true AND a.id IS NULL ORDER BY q.displayOrder")
    @NotNull
    List<Question> findPendingByProfileId(@Param("profileId") Long profileId);
}
