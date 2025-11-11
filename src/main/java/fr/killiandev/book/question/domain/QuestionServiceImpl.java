package fr.killiandev.book.question.domain;

import static fr.killiandev.book.observability.api.exception.ExceptionType.*;

import fr.killiandev.book.observability.api.exception.type.QuestionException;
import fr.killiandev.book.question.domain.dao.ProfileAnswerDao;
import fr.killiandev.book.question.domain.dao.QuestionDao;
import fr.killiandev.book.question.domain.dto.ListQuestionDto;
import fr.killiandev.book.question.domain.entity.ProfileAnswer;
import fr.killiandev.book.question.domain.entity.Question;
import fr.killiandev.book.question.domain.mapper.QuestionMapper;
import fr.killiandev.book.question.spi.QuestionProfileServiceSpi;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;
    private final ProfileAnswerDao profileAnswerDao;
    private final QuestionProfileServiceSpi questionProfileService;

    @Override
    public ListQuestionDto getActiveQuestions() {
        log.info("- Retrieving active questions");
        return ListQuestionDto.builder()
                .questions(questionDao.findActiveQuestions().stream()
                        .map(QuestionMapper.INSTANCE::of)
                        .toList())
                .build();
    }

    @Override
    public ListQuestionDto getPendingQuestions(String phoneNumber) {
        log.info("- Retrieving pending questions for user with phone number: {}", phoneNumber);
        long profileId = questionProfileService.getProfileId(phoneNumber);
        return ListQuestionDto.builder()
                .questions(questionDao.findPendingQuestions(profileId).stream()
                        .map(QuestionMapper.INSTANCE::of)
                        .toList())
                .build();
    }

    @NewSpan("QuestionService - submitAnswer")
    @Override
    public void submitAnswer(String phoneNumber, Long questionId, String answer) {
        log.info("- Submitting answer for user with phone number: {}", phoneNumber);
        long profileId = questionProfileService.getProfileId(phoneNumber);
        Question question = questionDao.findById(questionId);

        if (Boolean.FALSE.equals(question.getActive())) {
            log.info("- Question with id {} is not active", questionId);
            throw new QuestionException(QUESTION_NOT_ACTIVE_ERROR, "questionId", questionId);
        }

        if (Boolean.TRUE.equals(question.getMultipleResponse())) {
            validateMultipleAnswer(phoneNumber, question, answer);
        } else {
            validateSingleAnswer(phoneNumber, question, answer);
        }

        if (profileAnswerDao.existsByProfileAndQuestion(profileId, questionId)) {
            log.info("- Answer for question with id {} and phone number {} already exists", questionId, phoneNumber);
            throw new QuestionException(QUESTION_ALREADY_ANSWER_ERROR, "questionId", questionId);
        }

        log.info("- Saving answer for question with id {} and phone number {}", questionId, phoneNumber);
        profileAnswerDao.save(new ProfileAnswer(null, answer, profileId, question));
    }

    private void validateSingleAnswer(@NotNull String phoneNumber, @NotNull Question question, @NotNull String answer) {
        if (!question.getPossibleValues().getValues().contains(answer)) {
            log.info(
                    "- Answer for question with id {} and phone number {} is not valid", question.getId(), phoneNumber);
            throw new QuestionException(
                    QUESTION_INVALID_ANSWER_ERROR, "questionId", question.getId(), "answer", answer);
        }
    }

    private void validateMultipleAnswer(
            @NotNull String phoneNumber, @NotNull Question question, @NotNull String answer) {
        if (answer.charAt(answer.length() - 1) == ',') {
            log.info(
                    "- Answer for question with id {} and phone number {} is not valid", question.getId(), phoneNumber);
            throw new QuestionException(
                    QUESTION_INVALID_ANSWER_ERROR, "questionId", question.getId(), "answer", answer);
        }

        for (String ans : answer.split(",")) {
            if (!question.getPossibleValues().getValues().contains(ans)) {
                log.info(
                        "- Answer for question with id {} and phone number {} is not valid",
                        question.getId(),
                        phoneNumber);
                throw new QuestionException(
                        QUESTION_INVALID_ANSWER_ERROR, "questionId", question.getId(), "answer", ans);
            }
        }
    }
}
