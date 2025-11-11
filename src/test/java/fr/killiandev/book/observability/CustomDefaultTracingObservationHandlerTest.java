package fr.killiandev.book.observability;

import static fr.killiandev.book.observability.api.exception.ExceptionType.MAP_API_ERROR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import fr.killiandev.book.observability.api.exception.type.MapException;
import fr.killiandev.book.observability.domain.telemetry.CustomDefaultTracingObservationHandler;
import io.micrometer.observation.Observation;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.junit.jupiter.api.Test;

class CustomDefaultTracingObservationHandlerTest {

    @Test
    void tagsFunctionalExceptionsAsResolved() {
        Span span = mock(Span.class);
        CustomDefaultTracingObservationHandler handler = new TestHandler(span);
        Observation.Context context = new Observation.Context();
        context.setName("test");
        context.setError(new MapException(MAP_API_ERROR));

        handler.onStop(context);

        verify(span).tag("error.resolved", true);
    }

    @Test
    void ignoresNonFunctionalErrors() {
        Span span = mock(Span.class);
        CustomDefaultTracingObservationHandler handler = new TestHandler(span);
        Observation.Context context = new Observation.Context();
        context.setName("test");
        context.setError(new RuntimeException("boom"));

        handler.onStop(context);

        verify(span, never()).tag("error.resolved", true);
    }

    private static class TestHandler extends CustomDefaultTracingObservationHandler {
        private final Span span;

        private TestHandler(Span span) {
            super(Tracer.NOOP);
            this.span = span;
        }

        @Override
        public Span getRequiredSpan(Observation.Context context) {
            return span;
        }
    }
}
