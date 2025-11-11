package fr.killiandev.book.question.domain.mapper;

import fr.killiandev.book.question.domain.dto.ListQuestionDto;
import fr.killiandev.book.question.domain.entity.Question;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @NotNull
    ListQuestionDto.QuestionDto of(@NotNull Question question);
}
