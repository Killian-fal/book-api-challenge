package fr.killiandev.book.s3.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3BucketDto {
    MEDIA("media", "media-dev"),
    ;

    private final String name;
    private final String developmentName;
}
