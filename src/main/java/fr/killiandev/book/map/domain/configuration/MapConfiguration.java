package fr.killiandev.book.map.domain.configuration;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.maps.places.v1.PlacesClient;
import com.google.maps.places.v1.PlacesSettings;
import fr.killiandev.book.map.domain.MapService;
import fr.killiandev.book.map.domain.MapServiceImpl;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MapProperties.class)
public class MapConfiguration {

    private static final String FIELD_MASK =
            "places.id,places.displayName,places.location,places.formattedAddress,places.rating";

    @Bean
    public PlacesClient placesClient(MapProperties mapProperties) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-goog-fieldmask", FIELD_MASK);
        headers.put("x-goog-api-key", mapProperties.getApiKey());

        HeaderProvider headerProvider = FixedHeaderProvider.create(headers);

        PlacesSettings placesSettings = PlacesSettings.newBuilder()
                .setHeaderProvider(headerProvider)
                .setCredentialsProvider(NoCredentialsProvider.create())
                .build();

        return PlacesClient.create(placesSettings);
    }

    @Bean
    public MapService mapService(PlacesClient placesClient) {
        return new MapServiceImpl(placesClient);
    }
}
