package fr.killiandev.book.ai.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RecordingChatClientTestConfig {

    @Bean
    public RecordingChatClientBuilder chatClientBuilder() {
        return new RecordingChatClientBuilder();
    }
}
