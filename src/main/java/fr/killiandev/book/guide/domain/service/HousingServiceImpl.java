package fr.killiandev.book.guide.domain.service;

import fr.killiandev.book.guide.domain.HousingService;
import fr.killiandev.book.guide.domain.dao.HousingDao;
import fr.killiandev.book.guide.domain.dto.GetHousingDto;
import fr.killiandev.book.guide.domain.dto.GetHousingsDto;
import fr.killiandev.book.guide.domain.mapper.GuideMapper;
import fr.killiandev.book.guide.spi.GuideProfileServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class HousingServiceImpl implements HousingService {

    private final HousingDao housingDao;
    private final GuideProfileServiceSpi guideProfileServiceSpi;

    @Override
    public GetHousingsDto getHousings(String phoneNumber) {
        log.info("- Retrieving housings for user with phone number: {}", phoneNumber);
        GuideProfileDto profile = guideProfileServiceSpi.getProfile(phoneNumber);
        return GetHousingsDto.builder()
                .housings(housingDao.findByProfileId(profile.getId()).stream()
                        .map(GuideMapper.INSTANCE::of)
                        .toList())
                .build();
    }

    @Override
    public GetHousingDto getHousing(String phoneNumber, Long housingId) {
        log.info("- Retrieving housing with id: {} for user with phone number: {}", housingId, phoneNumber);
        GuideProfileDto profile = guideProfileServiceSpi.getProfile(phoneNumber);
        return GetHousingDto.builder()
                .housing(GuideMapper.INSTANCE.of(housingDao.findByIdAndProfileId(housingId, profile.getId())))
                .build();
    }
}
