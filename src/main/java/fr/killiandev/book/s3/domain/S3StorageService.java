package fr.killiandev.book.s3.domain;

import fr.killiandev.book.observability.api.exception.type.StoreS3Exception;
import fr.killiandev.book.s3.domain.dto.S3BucketDto;
import fr.killiandev.book.s3.domain.dto.S3ContentTypeDto;
import fr.killiandev.book.s3.domain.dto.S3FolderDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public interface S3StorageService {

    @NotNull
    String uploadFile(
            @Nullable String fileName,
            @NotNull byte[] file,
            @NotNull S3FolderDto s3Folder,
            @NotNull S3ContentTypeDto contentType,
            @NotNull S3BucketDto bucket)
            throws StoreS3Exception;

    @NotNull
    String getFileUrl(@NotNull String fileName, @NotNull S3FolderDto s3Folder, @NotNull S3BucketDto bucket)
            throws StoreS3Exception;
}
