package fr.killiandev.book.observability;

import static fr.killiandev.book.observability.api.exception.ExceptionType.CONSTRAINT_VIOLATION_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.ENTITY_NOT_FOUND;
import static fr.killiandev.book.observability.api.exception.ExceptionType.INVALID_ROLE;
import static fr.killiandev.book.observability.api.exception.ExceptionType.MAP_API_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.killiandev.book.observability.api.exception.type.MapException;
import fr.killiandev.book.observability.api.exception.type.NotFoundException;
import fr.killiandev.book.observability.domain.GlobalExceptionHandler;
import fr.killiandev.book.observability.domain.dto.ErrorResponseDto;
import fr.killiandev.book.observability.support.ExceptionHandlerTestConfig;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ExceptionHandlerTestConfig.class)
class GlobalExceptionHandlerTest {

    @MockitoBean
    private Tracer tracer;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    private Span span;

    @BeforeEach
    void setUpTracer() {
        span = mock(Span.class);
        TraceContext traceContext = mock(TraceContext.class);
        when(traceContext.traceId()).thenReturn("trace-123");
        when(span.context()).thenReturn(traceContext);
        when(tracer.currentSpan()).thenReturn(span);
    }

    @Test
    void handleNotFoundExceptionReturns404() {
        NotFoundException exception = new NotFoundException(ENTITY_NOT_FOUND, "id", "guide-42");

        var response = globalExceptionHandler.handleNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ErrorResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(404);
        assertThat(body.getError()).isEqualTo(ENTITY_NOT_FOUND);
        assertThat(body.getDetails()).containsEntry("id", "guide-42");
        assertThat(body.getTraceId()).isEqualTo("trace-123");
    }

    @Test
    void handleFunctionalExceptionReturns500() {
        MapException exception = new MapException(MAP_API_ERROR, "query", "Paris");

        var response = globalExceptionHandler.handleOther(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatus()).isEqualTo(500);
        assertThat(body.getError()).isEqualTo(MAP_API_ERROR);
        assertThat(body.getDetails()).containsEntry("query", "Paris");
        assertThat(body.getTraceId()).isEqualTo("trace-123");
    }

    @Test
    void handleConstraintViolationMapsViolations() {
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("request.latitude");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");
        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        var response = globalExceptionHandler.handleConstraint(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getError()).isEqualTo(CONSTRAINT_VIOLATION_ERROR);
        assertThat(body.getDetails()).containsEntry("request.latitude", "must not be null");
        assertThat(body.getTraceId()).isEqualTo("trace-123");
    }

    @Test
    void handleAccessDeniedFallsBackToNoTraceWhenSpanMissing() {
        when(tracer.currentSpan()).thenReturn(null);
        AccessDeniedException exception = new AccessDeniedException("forbidden");

        var response = globalExceptionHandler.handleAccessDeniedException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        ErrorResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getError()).isEqualTo(INVALID_ROLE);
        assertThat(body.getDetails()).isNull();
        assertThat(body.getTraceId()).isEqualTo("no-trace");
    }
}
