package fr.killiandev.book.observability.domain.configuration;

import fr.killiandev.book.observability.domain.telemetry.CustomDefaultTracingObservationHandler;
import fr.killiandev.book.observability.domain.telemetry.OpenTelemetryAppenderInitializer;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import io.opentelemetry.api.OpenTelemetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfiguration {
    @Bean
    public DefaultTracingObservationHandler defaultTracingObservationHandler(Tracer tracer) {
        return new CustomDefaultTracingObservationHandler(tracer);
    }

    @Bean
    public OpenTelemetryAppenderInitializer openTelemetryAppenderInitializer(OpenTelemetry openTelemetry) {
        return new OpenTelemetryAppenderInitializer(openTelemetry);
    }
}
