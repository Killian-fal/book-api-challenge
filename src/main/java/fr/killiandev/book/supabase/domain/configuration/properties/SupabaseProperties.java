package fr.killiandev.book.supabase.domain.configuration.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "supabase")
public class SupabaseProperties {

    @NotNull
    private String key;

    @NotNull
    private String url;

    @NotNull
    private String secret;
}
