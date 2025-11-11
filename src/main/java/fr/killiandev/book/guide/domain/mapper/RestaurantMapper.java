package fr.killiandev.book.guide.domain.mapper;

import static fr.killiandev.book.guide.domain.GuideConstant.GOOGLE_MAPS_SEARCH_URL_TEMPLATE;

import fr.killiandev.book.guide.domain.entity.guide.Restaurant;
import fr.killiandev.book.guide.spi.dto.GuideLocationResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    @Mapping(target = "name", source = "displayName")
    @Mapping(target = "description", constant = "")
    @Mapping(target = "address", source = "formattedAddress")
    @Mapping(target = "googleLink", source = "result", qualifiedByName = "generateGoogleLink")
    Restaurant of(GuideLocationResultDto result);

    @Named("generateGoogleLink")
    default String generateGoogleLink(GuideLocationResultDto result) {
        return GOOGLE_MAPS_SEARCH_URL_TEMPLATE.formatted(
                result.getDisplayName().trim().replace(" ", "+"), result.getPlaceId());
    }
}
