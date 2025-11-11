package fr.killiandev.book.guide.domain.mapper;

import fr.killiandev.book.guide.domain.dto.*;
import fr.killiandev.book.guide.domain.entity.Guide;
import fr.killiandev.book.guide.domain.entity.Housing;
import fr.killiandev.book.guide.domain.entity.guide.GeneralInfo;
import fr.killiandev.book.guide.spi.dto.GuideCreatedAiDto;
import fr.killiandev.book.guide.spi.dto.GuideTranslatedAiDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GuideMapper {

    GuideMapper INSTANCE = Mappers.getMapper(GuideMapper.class);

    GetHousingDto.GetHousingDtoHousing of(Housing housing);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "housing", ignore = true)
    @Mapping(target = "slug", ignore = true)
    Guide of(CreateGuideDto.CreateGuideDtoGuide guideDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "housing", ignore = true)
    @Mapping(target = "slug", ignore = true)
    Guide of(UpdateGuideDto.UpdateGuideDtoGuide guideDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "housing", ignore = true)
    @Mapping(target = "wifiInfo", ignore = true)
    @Mapping(target = "generalInfo.localisation", ignore = true)
    @Mapping(target = "slug", ignore = true)
    Guide of(GuideTranslatedAiDto guideTranslatedAiDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "language", ignore = true)
    @Mapping(target = "housing", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "restaurants", ignore = true)
    @Mapping(target = "generalInfo.localisation", ignore = true)
    @Mapping(target = "wifiInfo", source = "createGuideWithAiDto.wifiInfo")
    Guide of(GuideCreatedAiDto guideTranslatedAiDto, CreateGuideWithAiDto createGuideWithAiDto);

    GeneralInfo.Localisation of(CreateGuideWithAiDto.CreateGuideWithAiDtoLocalisation generalInfo);

    GetGuideDto of(Guide guide);

    AIGuideDto toAiDto(Guide guide);
}
