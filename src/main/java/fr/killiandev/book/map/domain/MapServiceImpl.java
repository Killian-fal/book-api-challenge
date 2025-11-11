package fr.killiandev.book.map.domain;

import static fr.killiandev.book.observability.api.exception.ExceptionType.MAP_API_ERROR;

import com.google.maps.places.v1.*;
import com.google.type.LatLng;
import fr.killiandev.book.map.domain.dto.PlaceRequestDto;
import fr.killiandev.book.observability.api.exception.type.MapException;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MapServiceImpl implements MapService {

    private static final int DEFAULT_RADIUS = 5000;

    private final PlacesClient placesClient;

    @NewSpan("MapService - searchPlaces")
    @Override
    public List<Place> searchPlaces(PlaceRequestDto placeRequestDto) {
        log.info("- Searching places with location: {}", placeRequestDto.getLocation());

        // for keyword : SearchTextRequest (setTextQuery)
        try {
            var location = placeRequestDto.getLocation();

            SearchNearbyRequest request = SearchNearbyRequest.newBuilder()
                    .setLocationRestriction(SearchNearbyRequest.LocationRestriction.newBuilder()
                            .setCircle(Circle.newBuilder()
                                    .setCenter(LatLng.newBuilder()
                                            .setLatitude(location.getLatitude())
                                            .setLongitude(location.getLongitude()))
                                    .setRadius(Objects.requireNonNullElse(placeRequestDto.getRadius(), DEFAULT_RADIUS))
                                    .build())
                            .build())
                    .addIncludedTypes("restaurant")
                    .setMaxResultCount(5)
                    .build();

            return placesClient.searchNearby(request).getPlacesList();

        } catch (Exception e) {
            throw new MapException(MAP_API_ERROR, e);
        }
    }
}
