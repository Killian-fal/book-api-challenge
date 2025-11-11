package fr.killiandev.book.guide;

import static fr.killiandev.book.observability.api.exception.ExceptionType.GUIDE_INVALID_AI_RESPONSE_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.GUIDE_INVALID_OWNER_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import fr.killiandev.book.guide.domain.GuideService;
import fr.killiandev.book.guide.domain.configuration.GuideConfiguration;
import fr.killiandev.book.guide.domain.dao.GuideDaoImpl;
import fr.killiandev.book.guide.domain.dao.HousingDaoImpl;
import fr.killiandev.book.guide.domain.dto.CreateGuideDto;
import fr.killiandev.book.guide.domain.dto.CreateGuideDto.CreateGuideDtoGeneralInfo;
import fr.killiandev.book.guide.domain.dto.CreateGuideDto.CreateGuideDtoGuide;
import fr.killiandev.book.guide.domain.dto.CreateGuideDto.CreateGuideDtoLocalisation;
import fr.killiandev.book.guide.domain.dto.CreateGuideDto.CreateGuideDtoRulesInfo;
import fr.killiandev.book.guide.domain.dto.CreateGuideDto.CreateGuideDtoTransportationInfo;
import fr.killiandev.book.guide.domain.dto.CreateGuideDto.CreateGuideDtoWifiInfo;
import fr.killiandev.book.guide.domain.dto.CreateGuideWithAiDto;
import fr.killiandev.book.guide.domain.dto.CreateGuideWithAiDto.CreateGuideWithAiDtoLocalisation;
import fr.killiandev.book.guide.domain.dto.CreateGuideWithAiDto.CreateGuideWithAiDtoWifiInfo;
import fr.killiandev.book.guide.domain.dto.GetGuideDto;
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
import fr.killiandev.book.guide.spi.dto.GuideAirbnbDataDto;
import fr.killiandev.book.guide.spi.dto.GuideCreatedAiDto;
import fr.killiandev.book.guide.spi.dto.GuideLocationResultDto;
import fr.killiandev.book.guide.spi.dto.GuideProfileDto;
import fr.killiandev.book.observability.api.exception.type.GuideException;
import java.util.ArrayList;
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
class GuideServiceIntegrationTest {

    private static final String PHONE_NUMBER = "+33601010101";

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private GuideService guideService;

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
    void createGuidePersistsHousingAndGeneratesSlug() {
        when(guideProfileServiceSpi.getProfile(PHONE_NUMBER)).thenReturn(new GuideProfileDto(42L));
        CreateGuideDto request = buildManualCreateRequest();

        GetGuideDto result = guideService.createGuide(PHONE_NUMBER, request);

        Guide persisted = guideRepository.findById(result.getGuide().getId()).orElseThrow();
        assertThat(persisted.getHousing().getProfileId()).isEqualTo(42L);
        assertThat(persisted.getSlug()).matches("\\d+-paris-loft");
        assertThat(persisted.getGeneralInfo().getPropertyName()).isEqualTo("Paris Loft");
        assertThat(persisted.getGeneralInfo().getLocalisation().getLatitude()).isEqualTo(48.8566);
        assertThat(persisted.getWifiInfo().getPassword()).isEqualTo("code-secret");
        assertThat(housingRepository.count()).isEqualTo(1);
    }

    @Test
    void createGuideWithAiStoresRestaurantsAndOverridesContactFlag() {
        when(guideProfileServiceSpi.getProfile(PHONE_NUMBER)).thenReturn(new GuideProfileDto(99L));
        when(guideScrapingServiceSpi.extractGuideData("https://airbnb.test/listing"))
                .thenReturn(new GuideAirbnbDataDto());
        GuideCreatedAiDto aiDto = new GuideCreatedAiDto();
        GuideCreatedAiDto.GeneralInfo generalInfo =
                new GuideCreatedAiDto.GeneralInfo("Seaside Flat", "Hello", false, "15h", "11h");
        aiDto.setGeneralInfo(generalInfo);
        aiDto.setRulesInfo(
                new GuideCreatedAiDto.RulesInfo("22h", true, "balcony only", false, "no pets", false, "no parties"));
        aiDto.setTransportationInfo(
                new GuideCreatedAiDto.TransportationInfo(true, "parking spot 12", "metro line 1", "call +3312345678"));
        when(guideAIServiceSpi.createGuide(anyString())).thenReturn(aiDto);

        GuideLocationResultDto restaurant =
                new GuideLocationResultDto("12 rue Oberkampf, Paris", "Chez Paul", 48.8666, 2.3777, "pid-123");
        when(guideMapServiceSpi.searchPlaces(43.2965, 5.3698)).thenReturn(List.of(restaurant));

        CreateGuideWithAiDto request = new CreateGuideWithAiDto(
                "https://airbnb.test/listing",
                "en",
                new CreateGuideWithAiDtoWifiInfo("Wifi", "wifi-secret"),
                new CreateGuideWithAiDtoLocalisation("Marseille", 43.2965, 5.3698));

        GetGuideDto result = guideService.createGuideWithAi(PHONE_NUMBER, request);

        Guide persisted = guideRepository.findById(result.getGuide().getId()).orElseThrow();
        assertThat(persisted.getLanguage()).isEqualTo("en");
        assertThat(persisted.getGeneralInfo().getPropertyName()).isEqualTo("Seaside Flat");
        assertThat(persisted.getGeneralInfo().getLocalisation().getAddress()).isEqualTo("Marseille");
        assertThat(persisted.getGeneralInfo().getShowContactInfo()).isTrue();
        assertThat(persisted.getWifiInfo().getNetworkName()).isEqualTo("Wifi");
        assertThat(persisted.getRestaurants()).hasSize(1);
        assertThat(persisted.getRestaurants().getFirst().getGoogleLink())
                .isEqualTo("https://www.google.com/maps/search/?api=1&query=Chez+Paul&query_place_id=pid-123");
        verify(guideMapServiceSpi).searchPlaces(eq(43.2965), eq(5.3698));
    }

    @Test
    void createGuideWithAiRejectsMissingPropertyName() {
        when(guideProfileServiceSpi.getProfile(PHONE_NUMBER)).thenReturn(new GuideProfileDto(12L));
        when(guideScrapingServiceSpi.extractGuideData(anyString())).thenReturn(new GuideAirbnbDataDto());
        GuideCreatedAiDto aiDto = new GuideCreatedAiDto();
        aiDto.setGeneralInfo(new GuideCreatedAiDto.GeneralInfo(null, "Welcome", true, "15h", "11h"));
        when(guideAIServiceSpi.createGuide(anyString())).thenReturn(aiDto);

        CreateGuideWithAiDto request = new CreateGuideWithAiDto(
                "https://airbnb.test/invalid",
                "fr",
                new CreateGuideWithAiDtoWifiInfo("net", "pwd"),
                new CreateGuideWithAiDtoLocalisation("Lyon", 45.75, 4.85));

        assertThatThrownBy(() -> guideService.createGuideWithAi(PHONE_NUMBER, request))
                .isInstanceOf(GuideException.class)
                .satisfies(ex -> assertThat(((GuideException) ex).getExceptionType())
                        .isEqualTo(GUIDE_INVALID_AI_RESPONSE_ERROR));

        assertThat(guideRepository.count()).isZero();
        assertThat(housingRepository.count()).isZero();
        verifyNoInteractions(guideMapServiceSpi);
    }

    @Test
    void deleteGuideRemovesHousingWhenLastGuideIsDeleted() {
        when(guideProfileServiceSpi.getProfile(PHONE_NUMBER)).thenReturn(new GuideProfileDto(77L));
        Housing housing = persistHousing(77L);
        Guide first = persistGuide(housing, "fr", "Loft Sud");
        Guide second = persistGuide(housing, "en", "Loft South");
        housing.setGuides(new ArrayList<>(List.of(first, second)));

        guideService.deleteGuide(PHONE_NUMBER, first.getId());

        assertThat(guideRepository.existsById(first.getId())).isFalse();
        assertThat(housingRepository.existsById(housing.getId())).isTrue();

        guideService.deleteGuide(PHONE_NUMBER, second.getId());

        assertThat(guideRepository.count()).isZero();
        assertThat(housingRepository.count()).isZero();
    }

    @Test
    void deleteGuideEnforcesOwnership() {
        when(guideProfileServiceSpi.getProfile(PHONE_NUMBER)).thenReturn(new GuideProfileDto(1L));
        Housing housing = persistHousing(42L);
        Guide guide = persistGuide(housing, "fr", "Loft Nord");

        assertThatThrownBy(() -> guideService.deleteGuide(PHONE_NUMBER, guide.getId()))
                .isInstanceOf(GuideException.class)
                .satisfies(ex ->
                        assertThat(((GuideException) ex).getExceptionType()).isEqualTo(GUIDE_INVALID_OWNER_ERROR));

        assertThat(guideRepository.count()).isEqualTo(1);
        assertThat(housingRepository.count()).isEqualTo(1);
    }

    private CreateGuideDto buildManualCreateRequest() {
        CreateGuideDtoLocalisation localisation =
                new CreateGuideDtoLocalisation("10 rue de Rivoli, Paris", 48.8566, 2.3522);
        CreateGuideDtoGeneralInfo generalInfo =
                new CreateGuideDtoGeneralInfo("Paris Loft", localisation, "Bienvenue", false, "15h", "11h");
        CreateGuideDtoWifiInfo wifiInfo = new CreateGuideDtoWifiInfo("LoftWifi", "code-secret");
        CreateGuideDtoRulesInfo rulesInfo = new CreateGuideDtoRulesInfo(
                "22h", true, "Balcon uniquement", false, "Pas d'animaux", false, "Pas de fêtes");
        CreateGuideDtoTransportationInfo transportationInfo =
                new CreateGuideDtoTransportationInfo(true, "Place 12", "Metro L1", "Taxi 123");

        CreateGuideDtoGuide guideDto = new CreateGuideDtoGuide();
        guideDto.setLanguage("fr");
        guideDto.setGeneralInfo(generalInfo);
        guideDto.setWifiInfo(wifiInfo);
        guideDto.setRulesInfo(rulesInfo);
        guideDto.setTransportationInfo(transportationInfo);

        CreateGuideDto request = new CreateGuideDto();
        request.setGuide(guideDto);
        return request;
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
                new Localisation("address " + language, 40.0 + language.length(), 2.0 + language.length()),
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
