package fr.killiandev.book.map.spi.mapper;

import com.google.maps.places.v1.Place;
import fr.killiandev.book.guide.spi.dto.GuideLocationResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapResultMapper {

    MapResultMapper INSTANCE = Mappers.getMapper(MapResultMapper.class);

    @Mapping(target = "formattedAddress", source = "formattedAddress")
    @Mapping(target = "displayName", source = "displayName.text")
    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "longitude", source = "location.longitude")
    @Mapping(target = "placeId", source = "id")
    GuideLocationResultDto of(Place result);
}
