package fr.killiandev.book.ai.domain;

import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

@Slf4j
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final ChatClient chatClient;

    @NewSpan("AIService - processInput")
    @Override
    public <T> T processInput(String prompt, Class<T> responseClass) {
        log.info("- Processing AI prompt...");
        return chatClient
                .prompt()
                .user(u -> u.text(prompt))
                .options(OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_1_NANO) // TODO: update to GPT-5-NANO
                        .build())
                .call()
                .entity(responseClass);
    }
}
