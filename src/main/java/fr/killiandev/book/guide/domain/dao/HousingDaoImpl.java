package fr.killiandev.book.guide.domain.dao;

import static fr.killiandev.book.common.util.ListUtil.toList;
import static fr.killiandev.book.observability.api.exception.ExceptionType.ENTITY_NOT_FOUND;

import fr.killiandev.book.guide.domain.entity.Housing;
import fr.killiandev.book.guide.domain.repository.HousingRepository;
import fr.killiandev.book.observability.api.exception.type.NotFoundException;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HousingDaoImpl implements HousingDao {

    private final HousingRepository repository;

    @NewSpan("HousingDao - findAll")
    @Override
    public List<Housing> findAll() {
        return toList(repository.findAll());
    }

    @NewSpan("HousingDao - findByIdOptional")
    @Override
    public Optional<Housing> findByIdOptional(Long id) {
        return repository.findById(id);
    }

    @NewSpan("HousingDao - findById")
    @Override
    public Housing findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Housing", "id", id));
    }

    @NewSpan("HousingDao - save")
    @Override
    public Housing save(Housing entity) {
        return repository.save(entity);
    }

    @NewSpan("HousingDao - delete")
    @Override
    public void delete(Housing entity) {
        repository.delete(entity);
    }

    @NewSpan("HousingDao - existsById")
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @NewSpan("HousingDao - findByProfileId")
    @Override
    public List<Housing> findByProfileId(Long profileId) {
        return repository.findByProfileId(profileId);
    }

    @NewSpan("HousingDao - findByIdAndProfileId")
    @Override
    public Housing findByIdAndProfileId(Long id, Long profileId) {
        return repository
                .findByIdAndProfileId(id, profileId)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Housing", "id", id));
    }
}
