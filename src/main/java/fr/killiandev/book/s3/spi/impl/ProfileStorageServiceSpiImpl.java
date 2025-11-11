package fr.killiandev.book.s3.spi.impl;

import static fr.killiandev.book.s3.domain.dto.S3ContentTypeDto.UNKNOWN;

import fr.killiandev.book.profile.spi.ProfileStorageServiceSpi;
import fr.killiandev.book.s3.domain.S3StorageService;
import fr.killiandev.book.s3.domain.dto.S3BucketDto;
import fr.killiandev.book.s3.domain.dto.S3ContentTypeDto;
import fr.killiandev.book.s3.domain.dto.S3FolderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProfileStorageServiceSpiImpl implements ProfileStorageServiceSpi {

    private static final String FORMAT = "book-%s";

    private final S3StorageService s3StorageService;

    @Override
    public String storeProfileAvatar(String fileName, byte[] data, String contentType) {
        S3ContentTypeDto s3ContentType = S3ContentTypeDto.fromString(contentType);

        if (UNKNOWN.equals(s3ContentType)) {
            log.warn("- Unknown content type for profile avatar: {}", contentType);
        }

        return s3StorageService.uploadFile(
                FORMAT.formatted(fileName), data, S3FolderDto.AVATARS, s3ContentType, S3BucketDto.MEDIA);
    }
}
