package fr.killiandev.book.guide.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetHousingsDto {

    @NotNull
    private List<GetHousingDto.GetHousingDtoHousing> housings;
}
