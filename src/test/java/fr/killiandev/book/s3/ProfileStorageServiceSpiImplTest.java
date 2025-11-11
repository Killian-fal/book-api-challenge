package fr.killiandev.book.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.killiandev.book.profile.spi.ProfileStorageServiceSpi;
import fr.killiandev.book.s3.domain.S3StorageService;
import fr.killiandev.book.s3.domain.dto.S3BucketDto;
import fr.killiandev.book.s3.domain.dto.S3ContentTypeDto;
import fr.killiandev.book.s3.domain.dto.S3FolderDto;
import fr.killiandev.book.s3.spi.configuration.S3SpiConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(S3SpiConfiguration.class)
class ProfileStorageServiceSpiImplTest {

    @MockitoBean
    private S3StorageService s3StorageService;

    @Autowired
    private ProfileStorageServiceSpi profileStorageServiceSpi;

    @Test
    void storeProfileAvatarUsesFormattedKeyAndContentType() {
        byte[] data = "img".getBytes();
        when(s3StorageService.uploadFile(any(), any(), any(), any(), any())).thenReturn("https://cdn/avatar");

        String url = profileStorageServiceSpi.storeProfileAvatar("123", data, "image/webp");

        assertThat(url).isEqualTo("https://cdn/avatar");
        verify(s3StorageService)
                .uploadFile(
                        eq("book-123"),
                        same(data),
                        eq(S3FolderDto.AVATARS),
                        eq(S3ContentTypeDto.IMAGE_WEBP),
                        eq(S3BucketDto.MEDIA));
    }

    @Test
    void unknownContentTypeFallsBackToOctetStreamAndLogsWarning() {
        byte[] data = "pdf".getBytes();
        when(s3StorageService.uploadFile(any(), any(), any(), any(), any())).thenReturn("url");
        profileStorageServiceSpi.storeProfileAvatar("45", data, "application/pdf");

        verify(s3StorageService)
                .uploadFile(
                        eq("book-45"),
                        same(data),
                        eq(S3FolderDto.AVATARS),
                        eq(S3ContentTypeDto.UNKNOWN),
                        eq(S3BucketDto.MEDIA));
    }
}
