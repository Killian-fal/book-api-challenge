package fr.killiandev.book.profile.domain.dao;

import static fr.killiandev.book.common.util.ListUtil.toList;
import static fr.killiandev.book.observability.api.exception.ExceptionType.ENTITY_NOT_FOUND;

import fr.killiandev.book.observability.api.exception.type.NotFoundException;
import fr.killiandev.book.profile.domain.entity.Profile;
import fr.killiandev.book.profile.domain.repository.ProfileRepository;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileDaoImpl implements ProfileDao {

    private final ProfileRepository repository;

    @NewSpan("ProfileDao - findAll")
    @Override
    public List<Profile> findAll() {
        return toList(repository.findAll());
    }

    @NewSpan("ProfileDao - findByIdOptional")
    @Override
    public Optional<Profile> findByIdOptional(Long id) {
        return repository.findById(id);
    }

    @NewSpan("ProfileDao - findById")
    @Override
    public Profile findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Profile", "id", id));
    }

    @NewSpan("ProfileDao - save")
    @Override
    public Profile save(Profile entity) {
        return repository.save(entity);
    }

    @NewSpan("ProfileDao - delete")
    @Override
    public void delete(Profile entity) {
        repository.delete(entity);
    }

    @NewSpan("ProfileDao - existsById")
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @NewSpan("ProfileDao - findByPhoneNumberOptional")
    @Override
    public Optional<Profile> findByPhoneNumberOptional(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    @NewSpan("ProfileDao - findByPhoneNumber")
    @Override
    public Profile findByPhoneNumber(String phoneNumber) {
        return repository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(
                        () -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Profile", "phoneNumber", phoneNumber));
    }
}
