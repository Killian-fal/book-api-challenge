package fr.killiandev.book.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.killiandev.book.ai.domain.AIService;
import fr.killiandev.book.ai.domain.configuration.AIConfiguration;
import fr.killiandev.book.ai.spi.configuration.AISpiConfiguration;
import fr.killiandev.book.ai.support.RecordingChatClientTestConfig;
import fr.killiandev.book.guide.spi.GuideAIServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideCreatedAiDto;
import fr.killiandev.book.guide.spi.dto.GuideTranslatedAiDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({RecordingChatClientTestConfig.class, AIConfiguration.class, AISpiConfiguration.class})
class GuideAiServiceSpiIntegrationTest {

    @MockitoBean
    private AIService aiService;

    @Autowired
    private GuideAIServiceSpi guideAIServiceSpi;

    @Test
    void translateGuideDelegatesToAiService() {
        GuideTranslatedAiDto expected = new GuideTranslatedAiDto();
        when(aiService.processInput("prompt", GuideTranslatedAiDto.class)).thenReturn(expected);

        GuideTranslatedAiDto response = guideAIServiceSpi.translateGuide("prompt");

        assertThat(response).isSameAs(expected);
        verify(aiService).processInput("prompt", GuideTranslatedAiDto.class);
    }

    @Test
    void createGuideDelegatesToAiService() {
        GuideCreatedAiDto expected = new GuideCreatedAiDto();
        when(aiService.processInput("create", GuideCreatedAiDto.class)).thenReturn(expected);

        GuideCreatedAiDto response = guideAIServiceSpi.createGuide("create");

        assertThat(response).isSameAs(expected);
        verify(aiService).processInput("create", GuideCreatedAiDto.class);
    }
}
