package fr.killiandev.book.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.maps.places.v1.Place;
import com.google.type.LatLng;
import com.google.type.LocalizedText;
import fr.killiandev.book.guide.spi.GuideMapServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideLocationResultDto;
import fr.killiandev.book.map.domain.MapService;
import fr.killiandev.book.map.domain.dto.PlaceRequestDto;
import fr.killiandev.book.map.spi.configuration.MapSpiConfiguration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(MapSpiConfiguration.class)
class GuideMapServiceSpiIntegrationTest {

    @Autowired
    private GuideMapServiceSpi guideMapServiceSpi;

    @MockitoBean
    private MapService mapService;

    @Test
    void searchPlacesFiltersOutLocationsBelowMinimalRating() {
        when(mapService.searchPlaces(any(PlaceRequestDto.class)))
                .thenReturn(List.of(
                        place("low", 3.2, "Too Low", "addr-1", 40.0, -3.0),
                        place("ok", 4.6, "Great Spot", "addr-2", 41.0, -4.0)));

        List<GuideLocationResultDto> results = guideMapServiceSpi.searchPlaces(10.0, 20.0);

        assertThat(results).hasSize(1);
        GuideLocationResultDto dto = results.getFirst();
        assertThat(dto.getPlaceId()).isEqualTo("ok");
        assertThat(dto.getDisplayName()).isEqualTo("Great Spot");
        assertThat(dto.getFormattedAddress()).isEqualTo("addr-2");
        assertThat(dto.getLatitude()).isEqualTo(41.0);
        assertThat(dto.getLongitude()).isEqualTo(-4.0);
    }

    private static Place place(
            String id, double rating, String displayName, String address, double latitude, double longitude) {
        return Place.newBuilder()
                .setId(id)
                .setRating(rating)
                .setDisplayName(LocalizedText.newBuilder().setText(displayName).build())
                .setFormattedAddress(address)
                .setLocation(LatLng.newBuilder()
                        .setLatitude(latitude)
                        .setLongitude(longitude)
                        .build())
                .build();
    }
}
