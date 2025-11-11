package fr.killiandev.book.guide.spi;

import fr.killiandev.book.guide.spi.dto.GuideAirbnbDataDto;

public interface GuideScrapingServiceSpi {

    GuideAirbnbDataDto extractGuideData(String airbnbUrl);
}
