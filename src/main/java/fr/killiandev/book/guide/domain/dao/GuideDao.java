package fr.killiandev.book.guide.domain.dao;

import fr.killiandev.book.common.Dao;
import fr.killiandev.book.guide.domain.entity.Guide;
import fr.killiandev.book.guide.domain.entity.Housing;
import java.util.Optional;

public interface GuideDao extends Dao<Guide, Long> {

    Guide findByIdWithHousing(Long id);

    Guide findBySlugWithHousing(String slug);

    Optional<Guide> findByLanguageAndHousing(String language, Housing housing);
}
