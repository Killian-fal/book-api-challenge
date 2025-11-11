package fr.killiandev.book.scraping.spi.impl;

import fr.killiandev.book.guide.spi.GuideScrapingServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideAirbnbDataDto;
import fr.killiandev.book.scraping.domain.ScrapingService;
import fr.killiandev.book.scraping.spi.mapper.ScrapingSpiMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GuideScrapingServiceSpiImpl implements GuideScrapingServiceSpi {

    private final ScrapingService scrapingService;

    @Override
    public GuideAirbnbDataDto extractGuideData(String airbnbUrl) {
        return ScrapingSpiMapper.INSTANCE.of(scrapingService.extractAirbnbData(airbnbUrl));
    }
}
