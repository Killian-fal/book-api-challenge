package fr.killiandev.book.observability.domain.dto;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {

    private int status;

    @NotNull
    private ExceptionType error;

    @NotNull
    private Map<String, Object> details;

    private long timestamp;

    private String traceId;
}
