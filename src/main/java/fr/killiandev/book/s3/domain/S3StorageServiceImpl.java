package fr.killiandev.book.s3.domain;

import static fr.killiandev.book.observability.api.exception.ExceptionType.S3_FIND_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.S3_STORAGE_ERROR;

import fr.killiandev.book.observability.api.exception.type.StoreS3Exception;
import fr.killiandev.book.s3.domain.configuration.S3Properties;
import fr.killiandev.book.s3.domain.dto.S3BucketDto;
import fr.killiandev.book.s3.domain.dto.S3ContentTypeDto;
import fr.killiandev.book.s3.domain.dto.S3FolderDto;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.ContentStreamProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Slf4j
@RequiredArgsConstructor
public class S3StorageServiceImpl implements S3StorageService {

    private final S3Client s3Client;
    private final S3Properties properties;

    @Observed(name = "s3.upload", contextualName = "S3StorageService - uploadFile")
    @Override
    public String uploadFile(
            String fileName, byte[] file, S3FolderDto folder, S3ContentTypeDto s3ContentType, S3BucketDto bucket)
            throws StoreS3Exception {
        try {
            String usedFileName = fileName;
            if (usedFileName == null) {
                usedFileName = UUID.randomUUID().toString();
            }

            String bucketName = getBucketName(bucket);
            log.info("- Uploading file to S3: bucketName={}, fileName={}", bucketName, usedFileName);

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("%s/%s".formatted(folder.getFolderName(), usedFileName))
                    .contentType(s3ContentType.getContentType())
                    .build();

            s3Client.putObject(
                    objectRequest,
                    RequestBody.fromContentProvider(
                            ContentStreamProvider.fromByteArray(file), file.length, s3ContentType.getContentType()));
            return generateFileUrl(usedFileName, folder, bucket);
        } catch (Exception e) {
            throw new StoreS3Exception(S3_STORAGE_ERROR, e, "bucket", bucket.name());
        }
    }

    @Observed(name = "s3.get", contextualName = "S3StorageService - getFileUrl")
    @Override
    public String getFileUrl(String fileName, S3FolderDto folder, S3BucketDto bucket) throws S3Exception {
        try {
            String bucketName = getBucketName(bucket);

            log.info("- Get file from S3: bucketName={}, fileName={}", bucketName, fileName);
            GetObjectRequest getObjectRequest =
                    GetObjectRequest.builder().bucket(bucketName).key(fileName).build();
            s3Client.getObject(getObjectRequest);

            return generateFileUrl(fileName, folder, bucket);
        } catch (Exception e) {
            throw new StoreS3Exception(S3_FIND_ERROR, e, "bucket", bucket.name());
        }
    }

    @NotNull
    private String getBucketName(@NotNull S3BucketDto bucket) {
        return Boolean.TRUE.equals(properties.getModeDev()) ? bucket.getDevelopmentName() : bucket.getName();
    }

    @NotNull
    private String generateFileUrl(@NotNull String fileName, @NotNull S3FolderDto folder, @NotNull S3BucketDto bucket) {
        return String.format("%s/%s/%s", properties.getPublicEndpoint().get(bucket), folder.getFolderName(), fileName);
    }
}
