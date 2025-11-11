package fr.killiandev.book.observability.domain.telemetry;

import fr.killiandev.book.observability.api.exception.FunctionalException;
import io.micrometer.observation.Observation;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.access.AccessDeniedException;

public class CustomDefaultTracingObservationHandler extends DefaultTracingObservationHandler {

    public CustomDefaultTracingObservationHandler(Tracer tracer) {
        super(tracer);
    }

    @Override
    public void onStop(Observation.Context context) {
        Span span = getRequiredSpan(context);
        var error = context.getError();
        if (error instanceof FunctionalException
                || error instanceof AccessDeniedException
                || error instanceof ConstraintViolationException) {
            span.tag("error.resolved", true);
        }
        super.onStop(context);
    }
}
