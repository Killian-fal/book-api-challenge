package fr.killiandev.book.map.domain.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "map")
public class MapProperties {

    @NotNull
    private String apiKey;
}
