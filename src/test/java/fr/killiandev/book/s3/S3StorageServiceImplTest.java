package fr.killiandev.book.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import fr.killiandev.book.observability.api.exception.type.StoreS3Exception;
import fr.killiandev.book.s3.domain.S3StorageService;
import fr.killiandev.book.s3.domain.configuration.S3Configuration;
import fr.killiandev.book.s3.domain.configuration.S3Properties;
import fr.killiandev.book.s3.domain.dto.S3BucketDto;
import fr.killiandev.book.s3.domain.dto.S3ContentTypeDto;
import fr.killiandev.book.s3.domain.dto.S3FolderDto;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@SpringJUnitConfig({S3Configuration.class})
@TestPropertySource(
        properties = {
            "s3.access-key=fake-key",
            "s3.secret-key=secret-key",
            "s3.endpoint=https://s3.local",
            "s3.region=eu-west-1",
            "s3.public-endpoint.MEDIA=https://cdn.test/media",
        })
class S3StorageServiceImplTest {

    @Autowired
    private S3StorageService s3StorageService;

    @MockitoBean
    private S3Client s3Client;

    @Autowired
    private S3Properties properties;

    @AfterEach
    void resetMode() {
        properties.setModeDev(false);
    }

    @Test
    void uploadFileGeneratesUuidAndBuildsUrl() {
        byte[] data = "avatar".getBytes();
        UUID generated = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        try (MockedStatic<UUID> mockUuid = Mockito.mockStatic(UUID.class)) {
            mockUuid.when(UUID::randomUUID).thenReturn(generated);
            when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                    .thenReturn(null);

            String url = s3StorageService.uploadFile(
                    null, data, S3FolderDto.AVATARS, S3ContentTypeDto.IMAGE_WEBP, S3BucketDto.MEDIA);

            ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
            verify(s3Client).putObject(requestCaptor.capture(), any(RequestBody.class));

            PutObjectRequest request = requestCaptor.getValue();
            assertThat(request.bucket()).isEqualTo("media");
            assertThat(request.key()).isEqualTo("avatars/%s".formatted(generated));
            assertThat(request.contentType()).isEqualTo("image/webp");
            assertThat(url).isEqualTo("https://cdn.test/media/avatars/123e4567-e89b-12d3-a456-426614174000");
        }
    }

    @Test
    void uploadFileBuildsUrl() {
        byte[] data = "avatar".getBytes();

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(null);

        String url = s3StorageService.uploadFile(
                "TEST", data, S3FolderDto.AVATARS, S3ContentTypeDto.IMAGE_WEBP, S3BucketDto.MEDIA);

        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(requestCaptor.capture(), any(RequestBody.class));

        PutObjectRequest request = requestCaptor.getValue();
        assertThat(request.bucket()).isEqualTo("media");
        assertThat(request.key()).isEqualTo("avatars/TEST");
        assertThat(request.contentType()).isEqualTo("image/webp");
        assertThat(url).isEqualTo("https://cdn.test/media/avatars/TEST");
    }

    @Test
    void uploadFileWrapsClientErrors() {
        byte[] data = "oops".getBytes();
        AwsServiceException awsFailure = S3Exception.builder().message("denied").build();
        doThrow(awsFailure).when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        assertThatThrownBy(() -> s3StorageService.uploadFile(
                        "profile.png", data, S3FolderDto.AVATARS, S3ContentTypeDto.IMAGE_PNG, S3BucketDto.MEDIA))
                .isInstanceOf(StoreS3Exception.class)
                .hasCause(awsFailure)
                .satisfies(ex -> assertThat(((StoreS3Exception) ex).getExceptionType())
                        .isEqualTo(ExceptionType.S3_STORAGE_ERROR));
    }

    @Test
    void getFileUrlUsesDevelopmentBucketWhenEnabled() {
        properties.setModeDev(true);

        String url = s3StorageService.getFileUrl("book-99", S3FolderDto.AVATARS, S3BucketDto.MEDIA);

        ArgumentCaptor<GetObjectRequest> requestCaptor = ArgumentCaptor.forClass(GetObjectRequest.class);
        verify(s3Client).getObject(requestCaptor.capture());
        GetObjectRequest request = requestCaptor.getValue();
        assertThat(request.bucket()).isEqualTo("media-dev");
        assertThat(request.key()).isEqualTo("book-99");
        assertThat(url).isEqualTo("https://cdn.test/media/avatars/book-99");
    }
}
