package fr.killiandev.book.question.domain.dao;

import static fr.killiandev.book.common.util.ListUtil.toList;
import static fr.killiandev.book.observability.api.exception.ExceptionType.ENTITY_NOT_FOUND;

import fr.killiandev.book.observability.api.exception.type.NotFoundException;
import fr.killiandev.book.question.domain.entity.Question;
import fr.killiandev.book.question.domain.repository.QuestionRepository;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionDaoImpl implements QuestionDao {

    private final QuestionRepository repository;

    @NewSpan("QuestionDao - findAll")
    @Override
    public List<Question> findAll() {
        return toList(repository.findAll());
    }

    @NewSpan("QuestionDao - findByIdOptional")
    @Override
    public Optional<Question> findByIdOptional(Long id) {
        return repository.findById(id);
    }

    @NewSpan("QuestionDao - findById")
    @Override
    public Question findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Question", "id", id));
    }

    @NewSpan("QuestionDao - save")
    @Override
    public Question save(Question entity) {
        return repository.save(entity);
    }

    @NewSpan("QuestionDao - delete")
    @Override
    public void delete(Question entity) {
        repository.delete(entity);
    }

    @NewSpan("QuestionDao - existsById")
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @NewSpan("QuestionDao - findActiveQuestions")
    @Override
    public List<Question> findActiveQuestions() {
        return repository.findByActiveTrueOrderByDisplayOrder();
    }

    @NewSpan("QuestionDao - findPendingQuestions")
    @Override
    public List<Question> findPendingQuestions(Long profileId) {
        return repository.findPendingByProfileId(profileId);
    }
}
