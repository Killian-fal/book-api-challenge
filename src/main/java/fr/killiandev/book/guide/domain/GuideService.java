package fr.killiandev.book.guide.domain;

import fr.killiandev.book.guide.domain.dto.CreateGuideDto;
import fr.killiandev.book.guide.domain.dto.CreateGuideWithAiDto;
import fr.killiandev.book.guide.domain.dto.GetGuideDto;
import fr.killiandev.book.guide.domain.dto.UpdateGuideDto;

public interface GuideService {

    GetGuideDto getGuide(String phoneNumber, Long guideId);

    GetGuideDto updateGuide(String phoneNumber, UpdateGuideDto updateProfileDto);

    GetGuideDto createGuide(String phoneNumber, CreateGuideDto createGuideDto);

    GetGuideDto createGuideWithAi(String phoneNumber, CreateGuideWithAiDto createGuideDto);

    void deleteGuide(String phoneNumber, Long guideId);
}
