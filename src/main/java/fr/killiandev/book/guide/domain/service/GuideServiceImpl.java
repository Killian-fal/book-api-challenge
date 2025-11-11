package fr.killiandev.book.guide.domain.service;

import static fr.killiandev.book.guide.domain.GuideConstant.CREATE_PROMPT;
import static fr.killiandev.book.guide.domain.util.SlugUtil.formatSlug;
import static fr.killiandev.book.guide.domain.util.SlugUtil.toSlug;
import static fr.killiandev.book.observability.api.exception.ExceptionType.GUIDE_INVALID_AI_RESPONSE_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.GUIDE_INVALID_OWNER_ERROR;

import com.nimbusds.jose.shaded.gson.Gson;
import fr.killiandev.book.guide.domain.GuideService;
import fr.killiandev.book.guide.domain.dao.GuideDao;
import fr.killiandev.book.guide.domain.dao.HousingDao;
import fr.killiandev.book.guide.domain.dto.CreateGuideDto;
import fr.killiandev.book.guide.domain.dto.CreateGuideWithAiDto;
import fr.killiandev.book.guide.domain.dto.GetGuideDto;
import fr.killiandev.book.guide.domain.dto.UpdateGuideDto;
import fr.killiandev.book.guide.domain.entity.Guide;
import fr.killiandev.book.guide.domain.entity.Housing;
import fr.killiandev.book.guide.domain.entity.guide.Restaurant;
import fr.killiandev.book.guide.domain.mapper.GuideMapper;
import fr.killiandev.book.guide.domain.mapper.RestaurantMapper;
import fr.killiandev.book.guide.spi.GuideAIServiceSpi;
import fr.killiandev.book.guide.spi.GuideMapServiceSpi;
import fr.killiandev.book.guide.spi.GuideProfileServiceSpi;
import fr.killiandev.book.guide.spi.GuideScrapingServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideAirbnbDataDto;
import fr.killiandev.book.guide.spi.dto.GuideCreatedAiDto;
import fr.killiandev.book.guide.spi.dto.GuideProfileDto;
import fr.killiandev.book.observability.api.exception.type.GuideException;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class GuideServiceImpl implements GuideService {

    private static final Gson GSON = new Gson();

    private final HousingDao housingDao;
    private final GuideDao guideDao;
    private final GuideProfileServiceSpi guideProfileServiceSpi;
    private final GuideAIServiceSpi guideAIServiceSpi;
    private final GuideScrapingServiceSpi guideScrapingServiceSpi;
    private final GuideMapServiceSpi guideMapServiceSpi;

    @Override
    public GetGuideDto getGuide(String phoneNumber, Long guideId) {
        log.info("- Retrieving guide with id: {} for user with phone number: {}", guideId, phoneNumber);
        return GuideMapper.INSTANCE.of(getGuideIfOwner(phoneNumber, guideId));
    }

    @NewSpan("GuideService - updateGuide")
    @Transactional
    @Override
    public GetGuideDto updateGuide(String phoneNumber, UpdateGuideDto updateProfileDto) {
        log.info(
                "- Updating guide with id: {} for user with phone number: {}",
                updateProfileDto.getGuideId(),
                phoneNumber);
        Guide guide = getGuideIfOwner(phoneNumber, updateProfileDto.getGuideId());

        Guide newGuideData = GuideMapper.INSTANCE.of(updateProfileDto.getGuide());
        newGuideData.setId(guide.getId());
        newGuideData.setSlug(guide.getSlug());
        newGuideData.setHousing(guide.getHousing());
        newGuideData.setLanguage(guide.getLanguage());

        return GuideMapper.INSTANCE.of(guideDao.save(newGuideData));
    }

    @NewSpan("GuideService - createGuide")
    @Transactional
    @Override
    public GetGuideDto createGuide(String phoneNumber, CreateGuideDto createGuideDto) {
        log.info("- Creating guide for user with with phone number: {}", phoneNumber);
        GuideProfileDto profile = guideProfileServiceSpi.getProfile(phoneNumber);

        Housing housing = new Housing();
        housing.setProfileId(profile.getId());
        housing = housingDao.save(housing);

        Guide guide = GuideMapper.INSTANCE.of(createGuideDto.getGuide());
        guide.setHousing(housing);
        guide.setSlug(toSlug(formatSlug(guide)));
        guide = guideDao.save(guide);

        return GuideMapper.INSTANCE.of(guide);
    }

    @Observed(name = "guide.create.with.ai", contextualName = "GuideService - createGuideWithAi")
    @Transactional
    @Override
    public GetGuideDto createGuideWithAi(String phoneNumber, CreateGuideWithAiDto createGuideDto) {
        log.info("- Creating guide with AI for user with with phone number: {}", phoneNumber);
        GuideProfileDto profile = guideProfileServiceSpi.getProfile(phoneNumber);

        log.info("- Extracting data from Airbnb link: {}", createGuideDto.getLink());
        GuideAirbnbDataDto airbnbData = guideScrapingServiceSpi.extractGuideData(createGuideDto.getLink());

        log.info(
                "- Ask AI to create the guide with the extracted data and the language: {}",
                createGuideDto.getLanguage());
        GuideCreatedAiDto guideCreatedAiDto = guideAIServiceSpi.createGuide(
                CREATE_PROMPT.formatted(GSON.toJson(airbnbData), createGuideDto.getLanguage()));

        if (guideCreatedAiDto.getGeneralInfo().getPropertyName() == null) {
            throw new GuideException(GUIDE_INVALID_AI_RESPONSE_ERROR);
        }

        log.info("- AI has created the guide, call external api to get restaurants");
        var localisation = createGuideDto.getLocalisation();
        List<Restaurant> restaurants =
                guideMapServiceSpi.searchPlaces(localisation.getLatitude(), localisation.getLongitude()).stream()
                        .map(RestaurantMapper.INSTANCE::of)
                        .toList();

        log.info("- Saving the guide into the database");
        Housing housing = new Housing();
        housing.setProfileId(profile.getId());
        housing = housingDao.save(housing);

        Guide guide = GuideMapper.INSTANCE.of(guideCreatedAiDto, createGuideDto);
        guide.setHousing(housing);
        guide.setLanguage(createGuideDto.getLanguage());
        guide.getGeneralInfo().setShowContactInfo(true);
        guide.getGeneralInfo().setLocalisation(GuideMapper.INSTANCE.of(createGuideDto.getLocalisation()));
        guide.setRestaurants(restaurants);
        guide.setSlug(toSlug(formatSlug(guide)));
        guide = guideDao.save(guide);

        return GuideMapper.INSTANCE.of(guide);
    }

    @NewSpan("GuideService - deleteGuide")
    @Transactional
    @Override
    public void deleteGuide(String phoneNumber, Long guideId) {
        log.info("- Deleting guide with id: {} for user with phone number: {}", guideId, phoneNumber);
        Guide guide = getGuideIfOwner(phoneNumber, guideId);
        Housing housing = guide.getHousing();
        housing.getGuides().remove(guide);

        guideDao.delete(guide);
        if (housing.getGuides().isEmpty()) {
            log.info("- Deleting housing with id: {} as it has no more guides", housing.getId());
            housingDao.delete(housing);
        }
    }

    private Guide getGuideIfOwner(String phoneNumber, Long guideId) {
        GuideProfileDto profile = guideProfileServiceSpi.getProfile(phoneNumber);
        return getGuideIfOwner(profile, guideId);
    }

    private Guide getGuideIfOwner(GuideProfileDto profile, Long guideId) {
        Guide guide = guideDao.findByIdWithHousing(guideId);

        if (!Objects.equals(guide.getHousing().getProfileId(), profile.getId())) {
            throw new GuideException(GUIDE_INVALID_OWNER_ERROR);
        }

        return guide;
    }
}
