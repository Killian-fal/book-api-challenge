package fr.killiandev.book.ai.domain.configuration;

import static fr.killiandev.book.ai.domain.AIConstant.DEFAULT_PROMPT;

import fr.killiandev.book.ai.domain.AIService;
import fr.killiandev.book.ai.domain.AIServiceImpl;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfiguration {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.defaultSystem(DEFAULT_PROMPT).build();
    }

    @Bean
    public AIService aiService(ChatClient chatClient) {
        return new AIServiceImpl(chatClient);
    }
}
