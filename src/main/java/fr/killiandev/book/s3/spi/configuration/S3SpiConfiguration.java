package fr.killiandev.book.s3.spi.configuration;

import fr.killiandev.book.profile.spi.ProfileStorageServiceSpi;
import fr.killiandev.book.s3.domain.S3StorageService;
import fr.killiandev.book.s3.spi.impl.ProfileStorageServiceSpiImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3SpiConfiguration {

    @Bean
    public ProfileStorageServiceSpi profileStorageServiceSpi(S3StorageService s3StorageService) {
        return new ProfileStorageServiceSpiImpl(s3StorageService);
    }
}
