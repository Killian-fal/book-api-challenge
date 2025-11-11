package fr.killiandev.book.guide;

import static fr.killiandev.book.observability.api.exception.ExceptionType.GUIDE_INVALID_OWNER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import fr.killiandev.book.guide.domain.GuideTranslationService;
import fr.killiandev.book.guide.domain.configuration.GuideConfiguration;
import fr.killiandev.book.guide.domain.dao.GuideDaoImpl;
import fr.killiandev.book.guide.domain.dao.HousingDaoImpl;
import fr.killiandev.book.guide.domain.dto.GetGuideDto;
import fr.killiandev.book.guide.domain.dto.TranslateGuideDto;
import fr.killiandev.book.guide.domain.entity.Guide;
import fr.killiandev.book.guide.domain.entity.Housing;
import fr.killiandev.book.guide.domain.entity.guide.GeneralInfo;
import fr.killiandev.book.guide.domain.entity.guide.GeneralInfo.Localisation;
import fr.killiandev.book.guide.domain.entity.guide.RulesInfo;
import fr.killiandev.book.guide.domain.entity.guide.TransportationInfo;
import fr.killiandev.book.guide.domain.entity.guide.WifiInfo;
import fr.killiandev.book.guide.domain.repository.GuideRepository;
import fr.killiandev.book.guide.domain.repository.HousingRepository;
import fr.killiandev.book.guide.spi.GuideAIServiceSpi;
import fr.killiandev.book.guide.spi.GuideMapServiceSpi;
import fr.killiandev.book.guide.spi.GuideProfileServiceSpi;
import fr.killiandev.book.guide.spi.GuideScrapingServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideProfileDto;
import fr.killiandev.book.guide.spi.dto.GuideTranslatedAiDto;
import fr.killiandev.book.observability.api.exception.type.GuideException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@Import({GuideConfiguration.class, GuideDaoImpl.class, HousingDaoImpl.class})
class GuideTranslationServiceIntegrationTest {

    private static final String PHONE_NUMBER = "+33602020202";

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private GuideTranslationService guideTranslationService;

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private HousingRepository housingRepository;

    @MockitoBean
    private GuideProfileServiceSpi guideProfileServiceSpi;

    @MockitoBean
    private GuideAIServiceSpi guideAIServiceSpi;

    @MockitoBean
    private GuideScrapingServiceSpi guideScrapingServiceSpi;

    @MockitoBean
    private GuideMapServiceSpi guideMapServiceSpi;

    @BeforeEach
    void resetMocks() {
        reset(guideProfileServiceSpi, guideAIServiceSpi, guideScrapingServiceSpi, guideMapServiceSpi);
    }

    @Test
    void translateGuideCreatesNewRecordWithPreservedWifiAndLocalisation() {
        Housing housing = persistHousing(55L);
        Guide baseGuide = persistGuide(housing, "fr", "Loft Original");
        when(guideProfileServiceSpi.getProfile(PHONE_NUMBER)).thenReturn(new GuideProfileDto(55L));

        GuideTranslatedAiDto translatedPayload = new GuideTranslatedAiDto();
        GuideTranslatedAiDto.GeneralInfo generalInfo =
                new GuideTranslatedAiDto.GeneralInfo("Guide Espagnol", "Hola", true, "16h", "10h");
        translatedPayload.setGeneralInfo(generalInfo);
        translatedPayload.setRulesInfo(
                new GuideTranslatedAiDto.RulesInfo("22h", false, "No smoking", false, "No pets", false, "No parties"));
        translatedPayload.setTransportationInfo(
                new GuideTranslatedAiDto.TransportationInfo(true, "Garage 4", "Bus 21", "Taxi 123"));
        translatedPayload.setRestaurants(List.of());
        when(guideAIServiceSpi.translateGuide(anyString())).thenReturn(translatedPayload);

        GetGuideDto dto =
                guideTranslationService.translateGuide(PHONE_NUMBER, new TranslateGuideDto(baseGuide.getId(), "es"));

        Guide persisted = guideRepository.findById(dto.getGuide().getId()).orElseThrow();
        assertThat(persisted.getHousing().getId()).isEqualTo(housing.getId());
        assertThat(persisted.getLanguage()).isEqualTo("es");
        assertThat(persisted.getSlug()).endsWith("guide-espagnol");
        assertThat(persisted.getWifiInfo()).usingRecursiveComparison().isEqualTo(baseGuide.getWifiInfo());
        assertThat(persisted.getGeneralInfo().getLocalisation())
                .usingRecursiveComparison()
                .isEqualTo(baseGuide.getGeneralInfo().getLocalisation());
        assertThat(guideRepository.count()).isEqualTo(2);
    }

    @Test
    void translateGuideRejectsWhenOwnerMismatch() {
        Housing housing = persistHousing(11L);
        Guide baseGuide = persistGuide(housing, "fr", "Loft Privé");
        when(guideProfileServiceSpi.getProfile(PHONE_NUMBER)).thenReturn(new GuideProfileDto(999L));

        assertThatThrownBy(() -> guideTranslationService.translateGuide(
                        PHONE_NUMBER, new TranslateGuideDto(baseGuide.getId(), "en")))
                .isInstanceOf(GuideException.class)
                .satisfies(ex ->
                        assertThat(((GuideException) ex).getExceptionType()).isEqualTo(GUIDE_INVALID_OWNER_ERROR));
        assertThat(guideRepository.count()).isEqualTo(1);
    }

    @Test
    void getGuideWithSpecificLanguageReturnsOriginalWhenAlreadyInRequestedLanguage() {
        Housing housing = persistHousing(60L);
        Guide guide = persistGuide(housing, "fr", "Loft Paris");

        GetGuideDto dto = guideTranslationService.getGuideWithSpecificLanguage("fr", guide.getSlug());

        assertThat(dto.getGuide().getId()).isEqualTo(guide.getId());
    }

    @Test
    void getGuideWithSpecificLanguageReturnsExistingTranslation() {
        Housing housing = persistHousing(60L);
        Guide frenchGuide = persistGuide(housing, "fr", "Loft Paris");
        Guide englishGuide = persistGuide(housing, "en", "Paris Loft");

        GetGuideDto dto = guideTranslationService.getGuideWithSpecificLanguage("en", frenchGuide.getSlug());

        assertThat(dto.getGuide().getId()).isEqualTo(englishGuide.getId());
        verifyNoInteractions(guideAIServiceSpi);
    }

    @Test
    void getGuideWithSpecificLanguageFallsBackToEnglishWhenTranslationFails() {
        Housing housing = persistHousing(60L);
        Guide frenchGuide = persistGuide(housing, "fr", "Loft Paris");
        Guide englishGuide = persistGuide(housing, "en", "Paris Loft");
        housing.setGuides(List.of(frenchGuide, englishGuide));
        when(guideAIServiceSpi.translateGuide(anyString())).thenThrow(new RuntimeException("boom"));

        GetGuideDto dto = guideTranslationService.getGuideWithSpecificLanguage("es", frenchGuide.getSlug());

        assertThat(dto.getGuide().getId()).isEqualTo(englishGuide.getId());
        assertThat(guideRepository.count()).isEqualTo(2);
    }

    @Test
    void getGuideWithSpecificLanguageFallsBackToFirstGuideWhenEnglishUnavailable() {
        Housing housing = persistHousing(33L);
        Guide frenchGuide = persistGuide(housing, "fr", "Loft Paris");
        housing.setGuides(List.of(frenchGuide));
        when(guideAIServiceSpi.translateGuide(anyString())).thenThrow(new RuntimeException("boom"));

        GetGuideDto dto = guideTranslationService.getGuideWithSpecificLanguage("es", frenchGuide.getSlug());

        assertThat(dto.getGuide().getId()).isEqualTo(frenchGuide.getId());
        assertThat(guideRepository.count()).isEqualTo(1);
    }

    private Housing persistHousing(long profileId) {
        Housing housing = new Housing();
        housing.setProfileId(profileId);
        return housingRepository.save(housing);
    }

    private Guide persistGuide(Housing housing, String language, String propertyName) {
        Guide guide = new Guide();
        guide.setLanguage(language);
        guide.setSlug(
                "%s-%s-%d".formatted(language, propertyName.replace(" ", "-").toLowerCase(), System.nanoTime()));
        guide.setHousing(housing);
        guide.setGeneralInfo(new GeneralInfo(
                propertyName,
                new Localisation("address " + language, 41.0 + language.length(), 2.5 + language.length()),
                "Welcome " + language,
                true,
                "Checkin",
                "Checkout"));
        guide.setWifiInfo(new WifiInfo("wifi-" + language, "pwd"));
        guide.setRulesInfo(new RulesInfo("22h", false, "", false, "", false, ""));
        guide.setTransportationInfo(new TransportationInfo(true, "parking", "metro", "taxi"));
        return guideRepository.save(guide);
    }
}
