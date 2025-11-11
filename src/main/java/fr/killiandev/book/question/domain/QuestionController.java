package fr.killiandev.book.question.domain;

import fr.killiandev.book.question.domain.dto.ListQuestionDto;
import fr.killiandev.book.question.domain.dto.SubmitProfileAnswerRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "Get the list of active questions")
    @GetMapping("/active")
    public ListQuestionDto active() {
        log.info("Received request for active questions");
        return questionService.getActiveQuestions();
    }

    @Operation(summary = "Get the list of pending questions for the authenticated user")
    @GetMapping("/pending")
    public ListQuestionDto pending(Authentication authentication) {
        log.info("Received request for pending questions for user: {}", authentication.getName());
        return questionService.getPendingQuestions(authentication.getName());
    }

    @Operation(summary = "Submit an answer for a question for the authenticated user")
    @PostMapping("/submit")
    public void submit(Authentication authentication, @Valid @NotNull SubmitProfileAnswerRequestDto submitRequest) {
        log.info("Received answer submission for user: {}", authentication.getName());
        questionService.submitAnswer(
                authentication.getName(), submitRequest.getQuestionId(), submitRequest.getAnswer());
    }
}
