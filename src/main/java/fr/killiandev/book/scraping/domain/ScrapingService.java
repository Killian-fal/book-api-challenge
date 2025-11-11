package fr.killiandev.book.scraping.domain;

import fr.killiandev.book.scraping.domain.dto.AirbnbAnnounceDataDto;

public interface ScrapingService {

    AirbnbAnnounceDataDto extractAirbnbData(String url);
}
