package fr.killiandev.book.scraping;

import static fr.killiandev.book.observability.api.exception.ExceptionType.SCRAPING_INVALID_HTML_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.SCRAPING_INVALID_JSON_ERROR;
import static fr.killiandev.book.observability.api.exception.ExceptionType.SCRAPING_INVALID_URL_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.killiandev.book.observability.api.exception.type.ScrapingException;
import fr.killiandev.book.scraping.domain.ScrapingService;
import fr.killiandev.book.scraping.domain.configuration.ScrapingConfiguration;
import fr.killiandev.book.scraping.domain.dto.AirbnbAnnounceDataDto;
import fr.killiandev.book.scraping.support.TestAirbnbServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ScrapingConfiguration.class)
class ScrapingServiceIntegrationTest {

    @Autowired
    private ScrapingService scrapingService;

    @Test
    void rejectNonAirbnbUrl() {
        assertThatThrownBy(() -> scrapingService.extractAirbnbData("https://example.com/listing"))
                .isInstanceOf(ScrapingException.class)
                .satisfies(ex ->
                        assertThat(((ScrapingException) ex).getExceptionType()).isEqualTo(SCRAPING_INVALID_URL_ERROR));
    }

    @Test
    void extractFromGzipHtmlRemovesNullSections() throws Exception {
        String json =
                """
                {
                  "niobeClientData": [
                    [
                      "b",
                      {
                        "data": {
                          "presentation": {
                            "stayProductDetailPage": {
                              "sections": {
                                "sections": [
                                  { "section": null },
                                  {
                                    "section": {
                                      "__typename": "PdpHighlightsSection",
                                      "highlights": [
                                        { "title": "Pool", "subtitle": "Private" }
                                      ]
                                    }
                                  }
                                ]
                              }
                            }
                          }
                        },
                        "variables": {
                          "pdpSectionsRequest": {
                            "adults": "2",
                            "children": "0",
                            "pets": 0
                          }
                        }
                      }
                    ]
                  ]
                }
                """;
        String html = "<html><body><script id=\"data-deferred-state-0\">%s</script></body></html>".formatted(json);

        try (TestAirbnbServer server =
                TestAirbnbServer.start(200, headers -> headers.add("Content-Encoding", "gzip"), gzip(html))) {

            AirbnbAnnounceDataDto dto = scrapingService.extractAirbnbData(server.getAirbnbUrl());

            assertThat(dto).isNotNull();
            List<AirbnbAnnounceDataDto.SectionsSub> sections = dto.getData()
                    .getPresentation()
                    .getStayProductDetailPage()
                    .getSections()
                    .getSections();
            assertThat(sections)
                    .hasSize(1)
                    .first()
                    .extracting(AirbnbAnnounceDataDto.SectionsSub::getSection)
                    .isInstanceOf(AirbnbAnnounceDataDto.PdpHighlightsSection.class);
        }
    }

    @Test
    void missingScriptTriggersInvalidJsonCause() throws Exception {
        String html = "<html><body><p>No script here</p></body></html>";
        try (TestAirbnbServer server =
                TestAirbnbServer.start(200, headers -> {}, html.getBytes(StandardCharsets.UTF_8))) {
            assertThatThrownBy(() -> scrapingService.extractAirbnbData(server.getAirbnbUrl()))
                    .isInstanceOf(ScrapingException.class)
                    .satisfies(ex -> {
                        ScrapingException outer = (ScrapingException) ex;
                        assertThat(outer.getExceptionType()).isEqualTo(SCRAPING_INVALID_HTML_ERROR);
                        assertThat(outer.getCause()).isInstanceOf(ScrapingException.class);
                        ScrapingException cause = (ScrapingException) outer.getCause();
                        assertThat(cause.getExceptionType()).isEqualTo(SCRAPING_INVALID_JSON_ERROR);
                    });
        }
    }

    @Test
    void httpErrorYieldsInvalidHtmlWrappingInvalidUrlCause() throws Exception {
        byte[] body = "not found".getBytes(StandardCharsets.UTF_8);
        try (TestAirbnbServer server = TestAirbnbServer.start(404, headers -> {}, body)) {
            assertThatThrownBy(() -> scrapingService.extractAirbnbData(server.getAirbnbUrl()))
                    .isInstanceOf(ScrapingException.class)
                    .satisfies(ex -> {
                        ScrapingException outer = (ScrapingException) ex;
                        assertThat(outer.getExceptionType()).isEqualTo(SCRAPING_INVALID_HTML_ERROR);
                        assertThat(outer.getCause()).isInstanceOf(ScrapingException.class);
                        ScrapingException cause = (ScrapingException) outer.getCause();
                        assertThat(cause.getExceptionType()).isEqualTo(SCRAPING_INVALID_URL_ERROR);
                        assertThat(cause.getDebugInfo()).containsEntry("responseCode", 404);
                    });
        }
    }

    private static byte[] gzip(String content) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
                gzip.write(content.getBytes(StandardCharsets.UTF_8));
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
