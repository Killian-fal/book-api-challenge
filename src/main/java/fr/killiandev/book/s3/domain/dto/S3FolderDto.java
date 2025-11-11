package fr.killiandev.book.s3.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3FolderDto {
    AVATARS("avatars"),
    ;

    private final String folderName;
}
