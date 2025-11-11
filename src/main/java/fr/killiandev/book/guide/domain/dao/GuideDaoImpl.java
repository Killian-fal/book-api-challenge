package fr.killiandev.book.guide.domain.dao;

import static fr.killiandev.book.common.util.ListUtil.toList;
import static fr.killiandev.book.observability.api.exception.ExceptionType.ENTITY_NOT_FOUND;

import fr.killiandev.book.guide.domain.entity.Guide;
import fr.killiandev.book.guide.domain.entity.Housing;
import fr.killiandev.book.guide.domain.repository.GuideRepository;
import fr.killiandev.book.observability.api.exception.type.NotFoundException;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuideDaoImpl implements GuideDao {

    private final GuideRepository repository;

    @NewSpan("GuideDao - findAll")
    @Override
    public List<Guide> findAll() {
        return toList(repository.findAll());
    }

    @NewSpan("GuideDao - findByIdOptional")
    @Override
    public Optional<Guide> findByIdOptional(Long id) {
        return repository.findById(id);
    }

    @NewSpan("GuideDao - findById")
    @Override
    public Guide findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Guide", "id", id));
    }

    @NewSpan("GuideDao - save")
    @Override
    public Guide save(Guide entity) {
        return repository.save(entity);
    }

    @NewSpan("GuideDao - delete")
    @Override
    public void delete(Guide entity) {
        repository.delete(entity);
    }

    @NewSpan("GuideDao - existsById")
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @NewSpan("GuideDao - findByIdWithHousing")
    @Override
    public Guide findByIdWithHousing(Long id) {
        return repository
                .findByIdWithHousing(id)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Guide", "id", id));
    }

    @NewSpan("GuideDao - findBySlugWithHousing")
    @Override
    public Guide findBySlugWithHousing(String slug) {
        return repository
                .findBySlugWithHousing(slug)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Guide", "slug", slug));
    }

    @NewSpan("GuideDao - findByLanguageAndHousing")
    @Override
    public Optional<Guide> findByLanguageAndHousing(String language, Housing housing) {
        return repository.findByLanguageAndHousing(language, housing);
    }
}
