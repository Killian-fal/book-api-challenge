package fr.killiandev.book.question;

import static fr.killiandev.book.observability.api.exception.ExceptionType.QUESTION_ALREADY_ANSWER_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.QUESTION_INVALID_ANSWER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.killiandev.book.observability.api.exception.type.QuestionException;
import fr.killiandev.book.question.domain.QuestionService;
import fr.killiandev.book.question.domain.configuration.QuestionConfiguration;
import fr.killiandev.book.question.domain.dao.ProfileAnswerDaoImpl;
import fr.killiandev.book.question.domain.dao.QuestionDaoImpl;
import fr.killiandev.book.question.domain.dto.ListQuestionDto;
import fr.killiandev.book.question.domain.entity.ProfileAnswer;
import fr.killiandev.book.question.domain.entity.Question;
import fr.killiandev.book.question.domain.entity.Question.QuestionPossibleValues;
import fr.killiandev.book.question.domain.repository.ProfileAnswerRepository;
import fr.killiandev.book.question.domain.repository.QuestionRepository;
import fr.killiandev.book.question.spi.QuestionProfileServiceSpi;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@Import({QuestionConfiguration.class, QuestionDaoImpl.class, ProfileAnswerDaoImpl.class})
class QuestionServiceIntegrationTest {

    private static final String PHONE_NUMBER = "+33600009999";

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ProfileAnswerRepository profileAnswerRepository;

    @MockitoBean
    private QuestionProfileServiceSpi questionProfileServiceSpi;

    @BeforeEach
    void resetMocks() {
        reset(questionProfileServiceSpi);
    }

    @Test
    void getActiveQuestionsReturnsOnlyActiveOrderedByDisplayOrder() {
        persistQuestion("wifi", true, false, 11, List.of("yes", "no"));
        persistQuestion("parking", false, false, 12, List.of("yes", "no"));
        persistQuestion("terrace", true, true, 13, List.of("north", "south"));

        ListQuestionDto list = questionService.getActiveQuestions();

        assertThat(list.getQuestions()).hasSize(4); // +2 built-in questions
        assertThat(list.getQuestions())
                .extracting(ListQuestionDto.QuestionDto::getName)
                .contains("terrace", "wifi");
        assertThat(list.getQuestions())
                .extracting(ListQuestionDto.QuestionDto::getDisplayOrder)
                .contains(11, 13);
    }

    @Test
    void getPendingQuestionsFiltersOutAlreadyAnsweredQuestions() {
        Question q1 = persistQuestion("wifi", true, false, 4, List.of("yes", "no"));
        Question q2 = persistQuestion("parking", true, false, 5, List.of("yes", "no"));
        Question q3 = persistQuestion("gym", true, false, 6, List.of("yes", "no"));
        when(questionProfileServiceSpi.getProfileId(PHONE_NUMBER)).thenReturn(42L);
        persistAnswer(q1, 42L, "yes");
        persistAnswer(q3, 24L, "no");

        ListQuestionDto list = questionService.getPendingQuestions(PHONE_NUMBER);

        assertThat(list.getQuestions())
                .extracting(ListQuestionDto.QuestionDto::getName)
                .contains("parking", "gym");
        verify(questionProfileServiceSpi).getProfileId(PHONE_NUMBER);
    }

    @Test
    void submitAnswerRejectsValuesOutsideSingleChoiceList() {
        Question question = persistQuestion("wifi", true, false, 7, List.of("yes", "no"));
        when(questionProfileServiceSpi.getProfileId(PHONE_NUMBER)).thenReturn(7L);

        assertThatThrownBy(() -> questionService.submitAnswer(PHONE_NUMBER, question.getId(), "maybe"))
                .isInstanceOf(QuestionException.class)
                .satisfies(ex -> assertThat(((QuestionException) ex).getExceptionType())
                        .isEqualTo(QUESTION_INVALID_ANSWER_ERROR));
        assertThat(profileAnswerRepository.count()).isZero();
    }

    @Test
    void submitAnswerRejectsMultipleChoiceEndingWithComma() {
        Question question = persistQuestion("amenities", true, true, 8, List.of("wifi", "parking"));
        when(questionProfileServiceSpi.getProfileId(PHONE_NUMBER)).thenReturn(8L);

        assertThatThrownBy(() -> questionService.submitAnswer(PHONE_NUMBER, question.getId(), "wifi,"))
                .isInstanceOf(QuestionException.class)
                .satisfies(ex -> assertThat(((QuestionException) ex).getExceptionType())
                        .isEqualTo(QUESTION_INVALID_ANSWER_ERROR));
        assertThat(profileAnswerRepository.count()).isZero();
    }

    @Test
    void submitAnswerRejectsMultipleChoiceWithUnknownValue() {
        Question question = persistQuestion("amenities", true, true, 9, List.of("wifi", "parking"));
        when(questionProfileServiceSpi.getProfileId(PHONE_NUMBER)).thenReturn(9L);

        assertThatThrownBy(() -> questionService.submitAnswer(PHONE_NUMBER, question.getId(), "wifi,garden"))
                .isInstanceOf(QuestionException.class)
                .satisfies(ex -> assertThat(((QuestionException) ex).getExceptionType())
                        .isEqualTo(QUESTION_INVALID_ANSWER_ERROR));
        assertThat(profileAnswerRepository.count()).isZero();
    }

    @Test
    void submitAnswerPreventsDuplicatesForSameProfileAndQuestion() {
        Question question = persistQuestion("balcony", true, false, 10, List.of("yes", "no"));
        when(questionProfileServiceSpi.getProfileId(PHONE_NUMBER)).thenReturn(10L);

        questionService.submitAnswer(PHONE_NUMBER, question.getId(), "yes");
        assertThat(profileAnswerRepository.existsByProfileIdAndQuestion_Id(10L, question.getId()))
                .isTrue();

        assertThatThrownBy(() -> questionService.submitAnswer(PHONE_NUMBER, question.getId(), "yes"))
                .isInstanceOf(QuestionException.class)
                .satisfies(ex -> assertThat(((QuestionException) ex).getExceptionType())
                        .isEqualTo(QUESTION_ALREADY_ANSWER_ERROR));
        assertThat(profileAnswerRepository.count()).isEqualTo(1);
    }

    private Question persistQuestion(
            String name, boolean active, boolean multipleResponse, int displayOrder, List<String> possibleValues) {
        Question question = new Question();
        question.setName(name);
        question.setActive(active);
        question.setMultipleResponse(multipleResponse);
        question.setDisplayOrder(displayOrder);
        question.setPossibleValues(new QuestionPossibleValues(possibleValues));
        return questionRepository.save(question);
    }

    private void persistAnswer(Question question, long profileId, String answerValue) {
        profileAnswerRepository.save(new ProfileAnswer(null, answerValue, profileId, question));
    }
}
