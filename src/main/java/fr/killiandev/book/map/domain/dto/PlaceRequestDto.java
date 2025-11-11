package fr.killiandev.book.map.domain.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceRequestDto {

    @NotNull
    @Valid
    private PlaceRequestDtoLocation location;

    @Nullable
    private Integer radius;

    //    TODO: IN the future
    //    @Nullable
    //    private List<String> keywords;

    @ToString
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlaceRequestDtoLocation {

        @NotNull
        @Min(-90)
        @Max(90)
        private Double latitude;

        @Min(-180)
        @Max(180)
        @NotNull
        private Double longitude;
    }
}
