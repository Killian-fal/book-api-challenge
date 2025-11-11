package fr.killiandev.book.ai.support;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;

public class RecordingChatClient implements ChatClient {

    private final AtomicReference<String> lastPrompt = new AtomicReference<>();
    private final AtomicReference<ChatOptions> lastOptions = new AtomicReference<>();
    private volatile Object nextEntity;

    public void setNextEntity(Object nextEntity) {
        this.nextEntity = nextEntity;
    }

    public String getLastPrompt() {
        return lastPrompt.get();
    }

    public ChatOptions getLastOptions() {
        return lastOptions.get();
    }

    @Override
    public ChatClientRequestSpec prompt() {
        return new RecordingRequestSpec();
    }

    @Override
    public ChatClientRequestSpec prompt(String prompt) {
        lastPrompt.set(prompt);
        return new RecordingRequestSpec();
    }

    @Override
    public ChatClientRequestSpec prompt(Prompt prompt) {
        throw new UnsupportedOperationException("prompt(Prompt) not supported in RecordingChatClient");
    }

    @Override
    public Builder mutate() {
        throw new UnsupportedOperationException("mutate() not supported in RecordingChatClient");
    }

    private class RecordingRequestSpec implements ChatClientRequestSpec {

        @Override
        public ChatClientRequestSpec advisors(Consumer<AdvisorSpec> advisors) {
            return this;
        }

        @Override
        public ChatClientRequestSpec advisors(Advisor... advisors) {
            return this;
        }

        @Override
        public ChatClientRequestSpec advisors(List<Advisor> advisors) {
            return this;
        }

        @Override
        public ChatClientRequestSpec messages(Message... messages) {
            return this;
        }

        @Override
        public ChatClientRequestSpec messages(List<Message> messages) {
            return this;
        }

        @Override
        public <T extends ChatOptions> ChatClientRequestSpec options(T options) {
            lastOptions.set(options);
            return this;
        }

        @Override
        public ChatClientRequestSpec toolNames(String... toolNames) {
            return this;
        }

        @Override
        public ChatClientRequestSpec tools(Object... tools) {
            return this;
        }

        @Override
        public ChatClientRequestSpec toolCallbacks(ToolCallback... toolCallbacks) {
            return this;
        }

        @Override
        public ChatClientRequestSpec toolCallbacks(List<ToolCallback> toolCallbacks) {
            return this;
        }

        @Override
        public ChatClientRequestSpec toolCallbacks(ToolCallbackProvider... toolCallbackProviders) {
            return this;
        }

        @Override
        public ChatClientRequestSpec toolContext(Map<String, Object> toolContext) {
            return this;
        }

        @Override
        public ChatClientRequestSpec system(String text) {
            return this;
        }

        @Override
        public ChatClientRequestSpec system(Resource resource, Charset charset) {
            return this;
        }

        @Override
        public ChatClientRequestSpec system(Resource resource) {
            return this;
        }

        @Override
        public ChatClientRequestSpec system(Consumer<PromptSystemSpec> promptSystemSpec) {
            return this;
        }

        @Override
        public ChatClientRequestSpec user(String text) {
            lastPrompt.set(text);
            return this;
        }

        @Override
        public ChatClientRequestSpec user(Resource resource, Charset charset) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatClientRequestSpec user(Resource resource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatClientRequestSpec user(Consumer<PromptUserSpec> promptUserSpec) {
            promptUserSpec.accept(new RecordingPromptUserSpec());
            return this;
        }

        @Override
        public ChatClientRequestSpec templateRenderer(TemplateRenderer templateRenderer) {
            return this;
        }

        @Override
        public CallResponseSpec call() {
            return new RecordingCallResponseSpec();
        }

        @Override
        public StreamResponseSpec stream() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Builder mutate() {
            throw new UnsupportedOperationException();
        }
    }

    private class RecordingPromptUserSpec implements PromptUserSpec {

        @Override
        public PromptUserSpec text(String text) {
            lastPrompt.set(text);
            return this;
        }

        @Override
        public PromptUserSpec text(Resource resource, Charset charset) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PromptUserSpec text(Resource resource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PromptUserSpec params(Map<String, Object> params) {
            return this;
        }

        @Override
        public PromptUserSpec param(String name, Object value) {
            return this;
        }

        @Override
        public PromptUserSpec media(Media... media) {
            return this;
        }

        @Override
        public PromptUserSpec media(MimeType mimeType, java.net.URL url) {
            return this;
        }

        @Override
        public PromptUserSpec media(MimeType mimeType, Resource resource) {
            return this;
        }
    }

    private class RecordingCallResponseSpec implements CallResponseSpec {

        @Override
        public <T> T entity(ParameterizedTypeReference<T> typeReference) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T entity(StructuredOutputConverter<T> outputConverter) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T entity(Class<T> responseClass) {
            Object entity = Objects.requireNonNull(nextEntity);
            if (!responseClass.isInstance(entity)) {
                throw new IllegalStateException(
                        "Configured entity %s is not of type %s".formatted(entity.getClass(), responseClass));
            }
            return responseClass.cast(entity);
        }

        @Override
        public ChatClientResponse chatClientResponse() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ChatResponse chatResponse() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String content() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> ResponseEntity<ChatResponse, T> responseEntity(Class<T> responseType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> ResponseEntity<ChatResponse, T> responseEntity(ParameterizedTypeReference<T> typeReference) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> ResponseEntity<ChatResponse, T> responseEntity(StructuredOutputConverter<T> outputConverter) {
            throw new UnsupportedOperationException();
        }
    }
}
