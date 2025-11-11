package fr.killiandev.book.observability.domain;

import static fr.killiandev.book.observability.api.exception.ExceptionType.CONSTRAINT_VIOLATION_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.INVALID_ROLE;

import fr.killiandev.book.observability.api.exception.FunctionalException;
import fr.killiandev.book.observability.api.exception.type.NotFoundException;
import fr.killiandev.book.observability.domain.dto.ErrorResponseDto;
import io.micrometer.tracing.Tracer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Tracer tracer;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(@NotNull NotFoundException exception) {
        log.debug("Capture NotFoundException", exception);

        ErrorResponseDto error = ErrorResponseDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(exception.getExceptionType())
                .details(exception.getDebugInfo())
                .timestamp(System.currentTimeMillis())
                .traceId(traceId())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(@NotNull AccessDeniedException exception) {
        log.debug("Capture AccessDeniedException", exception);

        ErrorResponseDto error = ErrorResponseDto.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error(INVALID_ROLE)
                .timestamp(System.currentTimeMillis())
                .traceId(traceId())
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FunctionalException.class)
    public ResponseEntity<ErrorResponseDto> handleOther(@NotNull FunctionalException exception) {
        log.error("Capture {}", exception.getClass().getName(), exception);

        ErrorResponseDto error = ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(exception.getExceptionType())
                .details(exception.getDebugInfo())
                .timestamp(System.currentTimeMillis())
                .traceId(traceId())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraint(@NotNull ConstraintViolationException exception) {
        log.debug("Capture {}", exception.getClass().getName(), exception);

        ErrorResponseDto error = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(CONSTRAINT_VIOLATION_ERROR)
                .details(exception.getConstraintViolations().stream()
                        .collect(Collectors.toMap(
                                violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)))
                .timestamp(System.currentTimeMillis())
                .traceId(traceId())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private String traceId() {
        return tracer.currentSpan() != null ? tracer.currentSpan().context().traceId() : "no-trace";
    }
}
