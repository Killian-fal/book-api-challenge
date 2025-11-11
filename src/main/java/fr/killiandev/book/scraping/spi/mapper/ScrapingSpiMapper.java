package fr.killiandev.book.scraping.spi.mapper;

import fr.killiandev.book.guide.spi.dto.GuideAirbnbDataDto;
import fr.killiandev.book.scraping.domain.dto.AirbnbAnnounceDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScrapingSpiMapper {

    ScrapingSpiMapper INSTANCE = Mappers.getMapper(ScrapingSpiMapper.class);

    GuideAirbnbDataDto of(AirbnbAnnounceDataDto data);

    GuideAirbnbDataDto.GeneralListContentSection of(AirbnbAnnounceDataDto.GeneralListContentSection section);

    GuideAirbnbDataDto.AmenitiesSection of(AirbnbAnnounceDataDto.AmenitiesSection section);

    GuideAirbnbDataDto.PdpDescriptionSection of(AirbnbAnnounceDataDto.PdpDescriptionSection section);

    GuideAirbnbDataDto.PdpHighlightsSection of(AirbnbAnnounceDataDto.PdpHighlightsSection section);

    GuideAirbnbDataDto.PoliciesSection of(AirbnbAnnounceDataDto.PoliciesSection section);

    GuideAirbnbDataDto.LocationSection of(AirbnbAnnounceDataDto.LocationSection section);

    GuideAirbnbDataDto.PhotoTourModalSection of(AirbnbAnnounceDataDto.PhotoTourModalSection section);

    default GuideAirbnbDataDto.SectionItem map(AirbnbAnnounceDataDto.SectionItem section) {
        if (section instanceof AirbnbAnnounceDataDto.GeneralListContentSection s) {
            return of(s);
        } else if (section instanceof AirbnbAnnounceDataDto.AmenitiesSection s) {
            return of(s);
        } else if (section instanceof AirbnbAnnounceDataDto.PdpDescriptionSection s) {
            return of(s);
        } else if (section instanceof AirbnbAnnounceDataDto.PdpHighlightsSection s) {
            return of(s);
        } else if (section instanceof AirbnbAnnounceDataDto.PoliciesSection s) {
            return of(s);
        } else if (section instanceof AirbnbAnnounceDataDto.LocationSection s) {
            return of(s);
        } else if (section instanceof AirbnbAnnounceDataDto.PhotoTourModalSection s) {
            return of(s);
        }

        throw new IllegalArgumentException("Unknown section type: " + section);
    }
}
