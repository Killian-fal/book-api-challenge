package fr.killiandev.book.guide.domain.dto;

import fr.killiandev.book.guide.domain.validator.ISO639_1;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateGuideWithAiDto {

    @NotEmpty
    private String link;

    @ISO639_1
    @NotEmpty
    private String language;

    @Valid
    @NotNull
    private CreateGuideWithAiDtoWifiInfo wifiInfo;

    @Valid
    @NotNull
    private CreateGuideWithAiDtoLocalisation localisation;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideWithAiDtoWifiInfo {
        private String networkName;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideWithAiDtoLocalisation {
        @NotEmpty
        private String address;

        @NotNull
        private Double latitude;

        @NotNull
        private Double longitude;
    }
}
