package fr.killiandev.book.observability;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import fr.killiandev.book.observability.domain.telemetry.OpenTelemetryAppenderInitializer;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.log4j.appender.v2_17.OpenTelemetryAppender;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class OpenTelemetryAppenderInitializerTest {

    @Test
    void afterPropertiesSetInstallsAppender() {
        OpenTelemetry openTelemetry = mock(OpenTelemetry.class);
        OpenTelemetryAppenderInitializer initializer = new OpenTelemetryAppenderInitializer(openTelemetry);

        try (MockedStatic<OpenTelemetryAppender> mock = mockStatic(OpenTelemetryAppender.class)) {
            initializer.afterPropertiesSet();
            mock.verify(() -> OpenTelemetryAppender.install(openTelemetry));
        }
    }
}
