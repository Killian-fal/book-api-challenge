package fr.killiandev.book.map;

import static fr.killiandev.book.observability.api.exception.ExceptionType.MAP_API_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.maps.places.v1.Circle;
import com.google.maps.places.v1.PlacesClient;
import com.google.maps.places.v1.SearchNearbyRequest;
import com.google.maps.places.v1.SearchNearbyResponse;
import fr.killiandev.book.map.domain.MapService;
import fr.killiandev.book.map.domain.configuration.MapConfiguration;
import fr.killiandev.book.map.domain.dto.PlaceRequestDto;
import fr.killiandev.book.map.domain.dto.PlaceRequestDto.PlaceRequestDtoLocation;
import fr.killiandev.book.observability.api.exception.type.MapException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(MapConfiguration.class)
@TestPropertySource(properties = "map.api-key=fake-key")
class MapServiceIntegrationTest {

    @Autowired
    private MapService mapService;

    @MockitoBean
    private PlacesClient placesClient;

    @Test
    void searchPlacesBuildsDefaultRequest() {
        when(placesClient.searchNearby(any(SearchNearbyRequest.class)))
                .thenReturn(SearchNearbyResponse.newBuilder().build());

        PlaceRequestDto requestDto = PlaceRequestDto.builder()
                .location(new PlaceRequestDtoLocation(48.8566, 2.3522))
                .build();

        mapService.searchPlaces(requestDto);

        ArgumentCaptor<SearchNearbyRequest> captor = ArgumentCaptor.forClass(SearchNearbyRequest.class);
        verify(placesClient).searchNearby(captor.capture());

        SearchNearbyRequest request = captor.getValue();
        Circle circle = request.getLocationRestriction().getCircle();

        assertThat(request.getIncludedTypesList()).containsExactly("restaurant");
        assertThat(request.getMaxResultCount()).isEqualTo(5);
        assertThat(circle.getRadius()).isEqualTo(5000);
        assertThat(circle.getCenter().getLatitude()).isEqualTo(48.8566);
        assertThat(circle.getCenter().getLongitude()).isEqualTo(2.3522);
    }

    @Test
    void searchPlacesWrapsClientErrorsInMapException() {
        RuntimeException failure = new RuntimeException("boom");
        when(placesClient.searchNearby(any(SearchNearbyRequest.class))).thenThrow(failure);

        PlaceRequestDto requestDto = PlaceRequestDto.builder()
                .location(new PlaceRequestDtoLocation(40.0, -3.0))
                .build();

        assertThatThrownBy(() -> mapService.searchPlaces(requestDto))
                .isInstanceOf(MapException.class)
                .hasCause(failure)
                .satisfies(
                        ex -> assertThat(((MapException) ex).getExceptionType()).isEqualTo(MAP_API_ERROR));
    }
}
