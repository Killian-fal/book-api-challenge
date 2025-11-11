package fr.killiandev.book.question.domain.dao;

import static fr.killiandev.book.common.util.ListUtil.toList;
import static fr.killiandev.book.observability.api.exception.ExceptionType.ENTITY_NOT_FOUND;

import fr.killiandev.book.observability.api.exception.type.NotFoundException;
import fr.killiandev.book.question.domain.entity.ProfileAnswer;
import fr.killiandev.book.question.domain.repository.ProfileAnswerRepository;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileAnswerDaoImpl implements ProfileAnswerDao {

    private final ProfileAnswerRepository repository;

    @NewSpan("ProfileAnswerDao - findAll")
    @Override
    public List<ProfileAnswer> findAll() {
        return toList(repository.findAll());
    }

    @NewSpan("ProfileAnswerDao - findByIdOptional")
    @Override
    public Optional<ProfileAnswer> findByIdOptional(Long id) {
        return repository.findById(id);
    }

    @NewSpan("ProfileAnswerDao - findById")
    @Override
    public ProfileAnswer findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "ProfileAnswer", "id", id));
    }

    @NewSpan("ProfileAnswerDao - save")
    @Override
    public ProfileAnswer save(ProfileAnswer entity) {
        return repository.save(entity);
    }

    @NewSpan("ProfileAnswerDao - delete")
    @Override
    public void delete(ProfileAnswer entity) {
        repository.delete(entity);
    }

    @NewSpan("ProfileAnswerDao - existsById")
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @NewSpan("ProfileAnswerDao - existsByProfileAndQuestion")
    @Override
    public boolean existsByProfileAndQuestion(Long id, Long questionId) {
        return repository.existsByProfileIdAndQuestion_Id(id, questionId);
    }
}
