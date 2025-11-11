package fr.killiandev.book.map.spi.impl;

import fr.killiandev.book.guide.spi.GuideMapServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideLocationResultDto;
import fr.killiandev.book.map.domain.MapService;
import fr.killiandev.book.map.domain.dto.PlaceRequestDto;
import fr.killiandev.book.map.spi.mapper.MapResultMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class GuideMapServiceSpiImpl implements GuideMapServiceSpi {

    private static final double MINIMAL_RATE = 3.5D;

    private final MapService mapService;

    @Override
    public List<GuideLocationResultDto> searchPlaces(Double latitude, Double longitude) {
        var results = mapService.searchPlaces(PlaceRequestDto.builder()
                .location(new PlaceRequestDto.PlaceRequestDtoLocation(latitude, longitude))
                .build());

        return results.stream()
                .filter(place -> place.getRating() >= MINIMAL_RATE)
                .map(MapResultMapper.INSTANCE::of)
                .toList();
    }
}
