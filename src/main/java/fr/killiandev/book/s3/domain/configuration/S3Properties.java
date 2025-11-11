package fr.killiandev.book.s3.domain.configuration;

import fr.killiandev.book.s3.domain.dto.S3BucketDto;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "s3")
public class S3Properties {

    @NotNull
    private Boolean modeDev = Boolean.FALSE;

    @NotNull
    private String accessKey;

    @NotNull
    private String secretKey;

    @NotNull
    private String region;

    @NotNull
    private String endpoint;

    @NotNull
    private Map<S3BucketDto, String> publicEndpoint;
}
