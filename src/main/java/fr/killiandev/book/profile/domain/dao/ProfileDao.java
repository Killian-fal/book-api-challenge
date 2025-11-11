package fr.killiandev.book.profile.domain.dao;

import fr.killiandev.book.common.Dao;
import fr.killiandev.book.profile.domain.entity.Profile;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

public interface ProfileDao extends Dao<Profile, Long> {

    @NotNull
    Optional<Profile> findByPhoneNumberOptional(@NotNull String phoneNumber);

    @NotNull
    Profile findByPhoneNumber(@NotNull String phoneNumber);
}
