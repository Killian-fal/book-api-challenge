package fr.killiandev.book.question.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

@Getter
@Builder
public class ListQuestionDto {

    @NotNull
    private List<QuestionDto> questions;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionDto {

        @NotNull
        private Integer id;

        @NotNull
        private String name;

        @NotNull
        private QuestionPossibleValuesDto possibleValues;

        @NotNull
        private Integer displayOrder;

        @NotNull
        private Boolean active;

        @NotNull
        private Boolean multipleResponse;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionPossibleValuesDto {
        private List<String> values;
    }
}
