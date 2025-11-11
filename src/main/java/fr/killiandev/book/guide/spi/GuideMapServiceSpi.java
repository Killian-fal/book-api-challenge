package fr.killiandev.book.guide.spi;

import fr.killiandev.book.guide.spi.dto.GuideLocationResultDto;
import java.util.List;

public interface GuideMapServiceSpi {

    List<GuideLocationResultDto> searchPlaces(Double latitude, Double longitude);
}
