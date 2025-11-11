package fr.killiandev.book.guide.domain;

import fr.killiandev.book.guide.domain.dto.GetGuideDto;
import fr.killiandev.book.guide.domain.dto.TranslateGuideDto;

public interface GuideTranslationService {

    GetGuideDto translateGuide(String phoneNumber, TranslateGuideDto translateGuideDto);

    GetGuideDto getGuideWithSpecificLanguage(String language, String slug);
}
