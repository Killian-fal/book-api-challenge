package fr.killiandev.book.ai.spi.configuration;

import fr.killiandev.book.ai.domain.AIService;
import fr.killiandev.book.ai.spi.impl.GuideAIServiceSpiImpl;
import fr.killiandev.book.guide.spi.GuideAIServiceSpi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AISpiConfiguration {

    @Bean
    public GuideAIServiceSpi guideAIServiceSpi(AIService aiService) {
        return new GuideAIServiceSpiImpl(aiService);
    }
}
