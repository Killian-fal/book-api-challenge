package fr.killiandev.book.observability.support;

import fr.killiandev.book.observability.domain.GlobalExceptionHandler;
import io.micrometer.tracing.Tracer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ExceptionHandlerTestConfig {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(Tracer tracer) {
        return new GlobalExceptionHandler(tracer);
    }
}
