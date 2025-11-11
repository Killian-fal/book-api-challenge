package fr.killiandev.book.ai;

import static org.assertj.core.api.Assertions.assertThat;

import fr.killiandev.book.ai.domain.AIConstant;
import fr.killiandev.book.ai.domain.configuration.AIConfiguration;
import fr.killiandev.book.ai.support.RecordingChatClient;
import fr.killiandev.book.ai.support.RecordingChatClientBuilder;
import fr.killiandev.book.ai.support.RecordingChatClientTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({RecordingChatClientTestConfig.class, AIConfiguration.class})
class AiChatClientConfigurationTest {

    @Autowired
    private RecordingChatClientBuilder chatClientBuilder;

    @Autowired
    private ChatClient chatClient;

    @Test
    void chatClientUsesDefaultSystemPrompt() {
        assertThat(chatClientBuilder.getDefaultSystemPrompt()).isEqualTo(AIConstant.DEFAULT_PROMPT);
        assertThat(chatClient).isInstanceOf(RecordingChatClient.class);
    }
}
