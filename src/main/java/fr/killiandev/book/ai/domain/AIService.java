package fr.killiandev.book.ai.domain;

public interface AIService {

    <T> T processInput(String prompt, Class<T> responseClass);
}
