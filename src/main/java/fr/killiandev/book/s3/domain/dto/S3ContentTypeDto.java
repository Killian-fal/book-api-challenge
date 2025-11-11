package fr.killiandev.book.s3.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3ContentTypeDto {
    VIDEO_MP4("video/mp4"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_GIF("image/gif"),
    IMAGE_WEBP("image/webp"),
    UNKNOWN("application/octet-stream"),
    ;

    private final String contentType;

    public static S3ContentTypeDto fromString(String contentType) {
        return java.util.Arrays.stream(S3ContentTypeDto.values())
                .filter(type -> type.getContentType().equalsIgnoreCase(contentType))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
