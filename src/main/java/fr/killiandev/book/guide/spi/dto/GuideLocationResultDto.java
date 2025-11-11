package fr.killiandev.book.guide.spi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GuideLocationResultDto {

    private String formattedAddress;

    private String displayName;

    private Double latitude;

    private Double longitude;

    private String placeId;
}
