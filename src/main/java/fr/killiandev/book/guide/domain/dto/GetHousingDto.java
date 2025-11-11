package fr.killiandev.book.guide.domain.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetHousingDto {

    @NotNull
    private GetHousingDto.GetHousingDtoHousing housing;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetHousingDtoHousing {
        @NotNull
        private Long id;

        @NotNull
        private List<GetHousingDtoGuide> guides;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetHousingDtoGuide {

        @NotNull
        private Long id;

        @NotNull
        private String language;

        @Nullable
        private GetHousingDtoGeneralInfo generalInfo;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetHousingDtoGeneralInfo {

        @Nullable
        private String propertyName;
    }
}
