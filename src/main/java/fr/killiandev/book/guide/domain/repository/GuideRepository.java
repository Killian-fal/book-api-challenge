package fr.killiandev.book.guide.domain.repository;

import fr.killiandev.book.guide.domain.entity.Guide;
import fr.killiandev.book.guide.domain.entity.Housing;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GuideRepository extends CrudRepository<Guide, Long> {

    @Query("SELECT g FROM Guide g JOIN FETCH g.housing WHERE g.id = :id")
    Optional<Guide> findByIdWithHousing(@Param("id") Long id);

    @Query("SELECT g FROM Guide g JOIN FETCH g.housing WHERE g.slug = :slug")
    Optional<Guide> findBySlugWithHousing(@Param("slug") String slug);

    Optional<Guide> findByLanguageAndHousing(String language, Housing housing);
}
