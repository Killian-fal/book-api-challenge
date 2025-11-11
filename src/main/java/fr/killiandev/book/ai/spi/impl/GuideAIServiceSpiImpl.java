package fr.killiandev.book.ai.spi.impl;

import fr.killiandev.book.ai.domain.AIService;
import fr.killiandev.book.guide.spi.GuideAIServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideCreatedAiDto;
import fr.killiandev.book.guide.spi.dto.GuideTranslatedAiDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GuideAIServiceSpiImpl implements GuideAIServiceSpi {

    private final AIService aiService;

    @Override
    public GuideTranslatedAiDto translateGuide(String prompt) {
        return aiService.processInput(prompt, GuideTranslatedAiDto.class);
    }

    @Override
    public GuideCreatedAiDto createGuide(String prompt) {
        return aiService.processInput(prompt, GuideCreatedAiDto.class);
    }
}
