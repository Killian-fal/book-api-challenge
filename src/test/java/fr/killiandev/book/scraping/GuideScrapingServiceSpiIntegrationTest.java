package fr.killiandev.book.scraping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import fr.killiandev.book.guide.spi.GuideScrapingServiceSpi;
import fr.killiandev.book.guide.spi.dto.GuideAirbnbDataDto;
import fr.killiandev.book.scraping.domain.ScrapingService;
import fr.killiandev.book.scraping.domain.dto.AirbnbAnnounceDataDto;
import fr.killiandev.book.scraping.spi.configuration.ScrapingSpiConfiguration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ScrapingSpiConfiguration.class)
class GuideScrapingServiceSpiIntegrationTest {

    @MockitoBean
    private ScrapingService scrapingService;

    @Autowired
    private GuideScrapingServiceSpi guideScrapingServiceSpi;

    @Test
    void extractGuideDataDelegatesAndMapsSections() {
        AirbnbAnnounceDataDto source = sampleAnnouncement();
        when(scrapingService.extractAirbnbData("https://airbnb.test/guide")).thenReturn(source);

        GuideAirbnbDataDto result = guideScrapingServiceSpi.extractGuideData("https://airbnb.test/guide");

        assertThat(result.getData()
                        .getPresentation()
                        .getStayProductDetailPage()
                        .getSections()
                        .getSections())
                .hasSize(1)
                .first()
                .extracting(GuideAirbnbDataDto.SectionsSub::getSection)
                .isInstanceOf(GuideAirbnbDataDto.PdpDescriptionSection.class);

        GuideAirbnbDataDto.PdpDescriptionSection section = (GuideAirbnbDataDto.PdpDescriptionSection) result.getData()
                .getPresentation()
                .getStayProductDetailPage()
                .getSections()
                .getSections()
                .getFirst()
                .getSection();
        assertThat(section.getTitle()).isEqualTo("Description");
        assertThat(section.getHtmlDescription().getHtmlText()).isEqualTo("<p>Nice place</p>");
    }

    private static AirbnbAnnounceDataDto sampleAnnouncement() {
        AirbnbAnnounceDataDto.PdpDescriptionSection pdpSection = new AirbnbAnnounceDataDto.PdpDescriptionSection();
        pdpSection.setTitle("Description");
        AirbnbAnnounceDataDto.HtmlDescription description = new AirbnbAnnounceDataDto.HtmlDescription();
        description.setHtmlText("<p>Nice place</p>");
        pdpSection.setHtmlDescription(description);

        AirbnbAnnounceDataDto.SectionsSub sectionsSub = new AirbnbAnnounceDataDto.SectionsSub();
        sectionsSub.setSection(pdpSection);

        AirbnbAnnounceDataDto.SectionsMain sectionsMain = new AirbnbAnnounceDataDto.SectionsMain();
        sectionsMain.setSections(List.of(sectionsSub));

        AirbnbAnnounceDataDto.StayProductDetailPage detailPage = new AirbnbAnnounceDataDto.StayProductDetailPage();
        detailPage.setSections(sectionsMain);

        AirbnbAnnounceDataDto.Presentation presentation = new AirbnbAnnounceDataDto.Presentation();
        presentation.setStayProductDetailPage(detailPage);

        AirbnbAnnounceDataDto.Data data = new AirbnbAnnounceDataDto.Data();
        data.setPresentation(presentation);

        AirbnbAnnounceDataDto dto = new AirbnbAnnounceDataDto();
        dto.setData(data);
        return dto;
    }
}
