package fr.killiandev.book.question.domain.configuration;

import fr.killiandev.book.question.domain.QuestionService;
import fr.killiandev.book.question.domain.QuestionServiceImpl;
import fr.killiandev.book.question.domain.dao.ProfileAnswerDao;
import fr.killiandev.book.question.domain.dao.QuestionDao;
import fr.killiandev.book.question.spi.QuestionProfileServiceSpi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuestionConfiguration {

    @Bean
    public QuestionService questionService(
            QuestionDao questionDao,
            ProfileAnswerDao profileAnswerDao,
            QuestionProfileServiceSpi questionProfileServiceSpi) {
        return new QuestionServiceImpl(questionDao, profileAnswerDao, questionProfileServiceSpi);
    }
}
