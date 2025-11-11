package fr.killiandev.book.ai.support;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.PromptSystemSpec;
import org.springframework.ai.chat.client.ChatClient.PromptUserSpec;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.core.io.Resource;

public class RecordingChatClientBuilder implements ChatClient.Builder {

    private final RecordingChatClient chatClient = new RecordingChatClient();
    private String defaultSystemPrompt;

    public RecordingChatClient getChatClient() {
        return chatClient;
    }

    public String getDefaultSystemPrompt() {
        return defaultSystemPrompt;
    }

    @Override
    public ChatClient.Builder defaultAdvisors(Advisor... advisors) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultAdvisors(Consumer<ChatClient.AdvisorSpec> advisors) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultAdvisors(List<Advisor> advisors) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultOptions(ChatOptions options) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultUser(String text) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultUser(Resource resource, Charset charset) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultUser(Resource resource) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultUser(Consumer<PromptUserSpec> promptUserSpec) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultSystem(String text) {
        this.defaultSystemPrompt = text;
        return this;
    }

    @Override
    public ChatClient.Builder defaultSystem(Resource resource, Charset charset) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultSystem(Resource resource) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultSystem(Consumer<PromptSystemSpec> promptSystemSpec) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultTemplateRenderer(TemplateRenderer templateRenderer) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultToolNames(String... toolNames) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultTools(Object... tools) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultToolCallbacks(ToolCallback... toolCallbacks) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultToolCallbacks(List<ToolCallback> toolCallbacks) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultToolCallbacks(ToolCallbackProvider... toolCallbackProviders) {
        return this;
    }

    @Override
    public ChatClient.Builder defaultToolContext(Map<String, Object> toolContext) {
        return this;
    }

    @Override
    public ChatClient.Builder clone() {
        return this;
    }

    @Override
    public ChatClient build() {
        return chatClient;
    }
}
