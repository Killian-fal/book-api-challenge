package fr.killiandev.book.map.spi.configuration;

import fr.killiandev.book.guide.spi.GuideMapServiceSpi;
import fr.killiandev.book.map.domain.MapService;
import fr.killiandev.book.map.spi.impl.GuideMapServiceSpiImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapSpiConfiguration {

    @Bean
    public GuideMapServiceSpi guideMapServiceSpi(MapService mapService) {
        return new GuideMapServiceSpiImpl(mapService);
    }
}
