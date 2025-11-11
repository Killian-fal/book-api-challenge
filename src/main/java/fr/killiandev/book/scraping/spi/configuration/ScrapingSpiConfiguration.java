package fr.killiandev.book.scraping.spi.configuration;

import fr.killiandev.book.guide.spi.GuideScrapingServiceSpi;
import fr.killiandev.book.scraping.domain.ScrapingService;
import fr.killiandev.book.scraping.spi.impl.GuideScrapingServiceSpiImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScrapingSpiConfiguration {

    @Bean
    public GuideScrapingServiceSpi guideScrapingServiceSpi(ScrapingService scrapingService) {
        return new GuideScrapingServiceSpiImpl(scrapingService);
    }
}
