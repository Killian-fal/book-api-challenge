package fr.killiandev.book.guide.domain.repository;

import fr.killiandev.book.guide.domain.entity.Housing;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface HousingRepository extends CrudRepository<Housing, Long> {

    List<Housing> findByProfileId(Long profileId);

    Optional<Housing> findByIdAndProfileId(Long id, Long profileId);
}
