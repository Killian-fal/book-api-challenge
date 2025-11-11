package fr.killiandev.book.guide.spi;

import fr.killiandev.book.guide.spi.dto.GuideCreatedAiDto;
import fr.killiandev.book.guide.spi.dto.GuideTranslatedAiDto;

public interface GuideAIServiceSpi {

    GuideTranslatedAiDto translateGuide(String prompt);

    GuideCreatedAiDto createGuide(String prompt);
}
