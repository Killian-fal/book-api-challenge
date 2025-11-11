package fr.killiandev.book.scraping.domain.configuration;

import fr.killiandev.book.scraping.domain.ScrapingService;
import fr.killiandev.book.scraping.domain.ScrapingServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScrapingConfiguration {

    @Bean
    public ScrapingService scrapingService() {
        return new ScrapingServiceImpl();
    }
}
