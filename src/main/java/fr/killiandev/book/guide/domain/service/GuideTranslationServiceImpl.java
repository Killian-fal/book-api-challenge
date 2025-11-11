package fr.killiandev.book.guide.domain.service;

import static fr.killiandev.book.guide.domain.GuideConstant.TRANSLATE_PROMPT;
import static fr.killiandev.book.guide.domain.util.SlugUtil.formatSlug;
import static fr.killiandev.book.guide.domain.util.SlugUtil.toSlug;
import static fr.killiandev.book.observability.api.exception.ExceptionType.GUIDE_INVALID_OWNER_ERROR;

import com.nimbusds.jose.shaded.gson.Gson;
import fr.killiandev.book.guide.domain.GuideTranslationService;
import fr.killiandev.book.guide.domain.dao.GuideDao;
import fr.killiandev.book.guide.domain.dto.AIGuideDto;
import fr.killiandev.book.guide.domain.dto.GetGuideDto;
import fr.killiandev.book.guide.domain.dto.TranslateGuideDto;
import fr.killiandev.book.guide.domain.entity.Guide;
import fr.killiandev.book.guide.domain.entity.Housing;
import fr.killiandev.book.guide.domain.mapper.GuideMapper;
import fr.killiandev.book.guide.spi.GuideAIServiceSpi;
import fr.killiandev.book.guide.spi.GuideProfileServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideProfileDto;
import fr.killiandev.book.guide.spi.dto.GuideTranslatedAiDto;
import fr.killiandev.book.observability.api.exception.type.GuideException;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class GuideTranslationServiceImpl implements GuideTranslationService {

    private static final Gson GSON = new Gson();

    private final GuideDao guideDao;
    private final GuideProfileServiceSpi guideProfileServiceSpi;
    private final GuideAIServiceSpi guideAIServiceSpi;

    @NewSpan("GuideService - translateGuide")
    @Transactional
    @Override
    public GetGuideDto translateGuide(String phoneNumber, TranslateGuideDto translateGuideDto) {
        log.info(
                "Translating guide with id: {} for user with phone number: {} to {}",
                translateGuideDto.getGuideId(),
                phoneNumber,
                translateGuideDto.getLanguage());
        GuideProfileDto profile = guideProfileServiceSpi.getProfile(phoneNumber);

        Guide guide = getGuideIfOwner(profile, translateGuideDto.getGuideId());
        Guide translatedGuide = translateGuide(guide, translateGuideDto.getLanguage());
        return GuideMapper.INSTANCE.of(translatedGuide);
    }

    @Observed(name = "guide.public.retrieve", contextualName = "GuideService - getGuideWithSpecificLanguage")
    @Override
    public GetGuideDto getGuideWithSpecificLanguage(String language, String slug) {
        log.info("- Getting guide with language {} and slug {}", language, slug);
        Guide guide = guideDao.findBySlugWithHousing(slug);
        if (guide.getLanguage().equals(language)) {
            log.info("- 1. Retrieve the guide in the requested language: {}", language);
            return GuideMapper.INSTANCE.of(guide);
        }

        Housing housing = guide.getHousing();
        log.info("- Looking for an existing translation for the requested language in the housing {}", housing.getId());
        Optional<Guide> correspondingGuide = guideDao.findByLanguageAndHousing(language, housing);
        if (correspondingGuide.isPresent()) {
            log.info("- 2. Retrieve the guide in the requested language: {}", language);
            return GuideMapper.INSTANCE.of(correspondingGuide.get());
        }

        try {
            log.info("- Translating the guide to the requested language: {}", language);
            Guide translatedGuide = translateGuide(guide, language);
            log.info("- 3. Retrieve the newly translated guide in the requested language: {}", language);
            return GuideMapper.INSTANCE.of(translatedGuide);
        } catch (Exception exception) {
            log.warn("- Translation failed, fallback to another language", exception);
            List<Guide> guides = housing.getGuides();
            if (guides.isEmpty()) {
                log.error("- No guides found for housing {}, impossible ?!", housing.getId());
                throw exception;
            }

            log.info("- Trying to find an english version as fallback");
            Optional<Guide> englishGuide =
                    guides.stream().filter(g -> g.getLanguage().equals("en")).findFirst();
            if (englishGuide.isPresent()) {
                log.info("- 4. Retrieve the english guide as fallback");
                return GuideMapper.INSTANCE.of(englishGuide.get());
            }

            log.info("- 5. Retrieve the first guide available as fallback");
            return GuideMapper.INSTANCE.of(guides.getFirst());
        }
    }

    private Guide translateGuide(Guide guide, String language) {
        Housing housing = guide.getHousing();

        AIGuideDto aiGuideDto = GuideMapper.INSTANCE.toAiDto(guide);
        log.info("-- Asking AI to translate the guide to {}", language);
        GuideTranslatedAiDto guideTranslatedAiDto =
                guideAIServiceSpi.translateGuide(TRANSLATE_PROMPT.formatted(language, GSON.toJson(aiGuideDto)));

        Guide translatedGuide = GuideMapper.INSTANCE.of(guideTranslatedAiDto);
        translatedGuide.setSlug(toSlug(formatSlug(translatedGuide)));
        translatedGuide.setHousing(housing);
        translatedGuide.setLanguage(language);
        translatedGuide.setWifiInfo(guide.getWifiInfo());
        translatedGuide.getGeneralInfo().setLocalisation(guide.getGeneralInfo().getLocalisation());
        guideDao.save(translatedGuide);

        return translatedGuide;
    }

    private Guide getGuideIfOwner(GuideProfileDto profile, Long guideId) {
        Guide guide = guideDao.findByIdWithHousing(guideId);

        if (!Objects.equals(guide.getHousing().getProfileId(), profile.getId())) {
            throw new GuideException(GUIDE_INVALID_OWNER_ERROR);
        }

        return guide;
    }
}
