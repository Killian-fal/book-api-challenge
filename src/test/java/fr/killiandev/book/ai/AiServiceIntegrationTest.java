package fr.killiandev.book.ai;

import static org.assertj.core.api.Assertions.assertThat;

import fr.killiandev.book.ai.domain.AIService;
import fr.killiandev.book.ai.domain.configuration.AIConfiguration;
import fr.killiandev.book.ai.support.RecordingChatClient;
import fr.killiandev.book.ai.support.RecordingChatClientBuilder;
import fr.killiandev.book.ai.support.RecordingChatClientTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({RecordingChatClientTestConfig.class, AIConfiguration.class})
class AiServiceIntegrationTest {

    @Autowired
    private AIService aiService;

    @Autowired
    private RecordingChatClientBuilder chatClientBuilder;

    @Test
    void processInputPassesPromptAndOptionsToChatClient() {
        RecordingChatClient chatClient = chatClientBuilder.getChatClient();
        SampleResponse expected = new SampleResponse("ok");
        chatClient.setNextEntity(expected);

        SampleResponse response = aiService.processInput("TOTO", SampleResponse.class);

        assertThat(response).isEqualTo(expected);
        assertThat(chatClient.getLastPrompt()).isEqualTo("TOTO");
        assertThat(chatClient.getLastOptions()).isInstanceOf(OpenAiChatOptions.class);
        OpenAiChatOptions options = (OpenAiChatOptions) chatClient.getLastOptions();
        assertThat(options.getModel()).isEqualTo(OpenAiApi.ChatModel.GPT_4_1_NANO.getValue());
    }

    private record SampleResponse(String value) {}
}
