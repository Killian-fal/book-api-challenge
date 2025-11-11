package fr.killiandev.book.guide.domain.dao;

import fr.killiandev.book.common.Dao;
import fr.killiandev.book.guide.domain.entity.Housing;
import java.util.List;

public interface HousingDao extends Dao<Housing, Long> {

    List<Housing> findByProfileId(Long profileId);

    Housing findByIdAndProfileId(Long id, Long profileId);
}
