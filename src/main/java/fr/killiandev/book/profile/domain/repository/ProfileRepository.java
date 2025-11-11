package fr.killiandev.book.profile.domain.repository;

import fr.killiandev.book.profile.domain.entity.Profile;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

    @NotNull
    Optional<Profile> findByPhoneNumber(@NotNull String phoneNumber);
}
