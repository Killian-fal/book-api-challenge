package fr.killiandev.book.guide.domain.configuration;

import fr.killiandev.book.guide.domain.GuideService;
import fr.killiandev.book.guide.domain.GuideTranslationService;
import fr.killiandev.book.guide.domain.HousingService;
import fr.killiandev.book.guide.domain.dao.GuideDao;
import fr.killiandev.book.guide.domain.dao.HousingDao;
import fr.killiandev.book.guide.domain.service.GuideServiceImpl;
import fr.killiandev.book.guide.domain.service.GuideTranslationServiceImpl;
import fr.killiandev.book.guide.domain.service.HousingServiceImpl;
import fr.killiandev.book.guide.spi.GuideAIServiceSpi;
import fr.killiandev.book.guide.spi.GuideMapServiceSpi;
import fr.killiandev.book.guide.spi.GuideProfileServiceSpi;
import fr.killiandev.book.guide.spi.GuideScrapingServiceSpi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuideConfiguration {

    @Bean
    public GuideService guideService(
            HousingDao housingDao,
            GuideDao guideDao,
            GuideProfileServiceSpi guideProfileServiceSpi,
            GuideAIServiceSpi guideAIServiceSpi,
            GuideScrapingServiceSpi guideScrapingServiceSpi,
            GuideMapServiceSpi guideMapServiceSpi) {
        return new GuideServiceImpl(
                housingDao,
                guideDao,
                guideProfileServiceSpi,
                guideAIServiceSpi,
                guideScrapingServiceSpi,
                guideMapServiceSpi);
    }

    @Bean
    public GuideTranslationService guideTranslationService(
            GuideDao guideDao, GuideProfileServiceSpi guideProfileServiceSpi, GuideAIServiceSpi guideAIServiceSpi) {
        return new GuideTranslationServiceImpl(guideDao, guideProfileServiceSpi, guideAIServiceSpi);
    }

    @Bean
    public HousingService housingService(HousingDao housingDao, GuideProfileServiceSpi guideProfileServiceSpi) {
        return new HousingServiceImpl(housingDao, guideProfileServiceSpi);
    }
}
