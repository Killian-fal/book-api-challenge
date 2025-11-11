package fr.killiandev.book.scraping.domain;

import static fr.killiandev.book.observability.api.exception.ExceptionType.*;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import fr.killiandev.book.observability.api.exception.type.ScrapingException;
import fr.killiandev.book.scraping.domain.dto.AirbnbAnnounceDataDto;
import io.micrometer.observation.annotation.Observed;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ScrapingServiceImpl implements ScrapingService {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AirbnbAnnounceDataDto.SectionItem.class, new SectionDeserializer())
            .create();

    @Observed(name = "scraping.extract", contextualName = "ScrapingService - extractAirbnbData")
    @Override
    public AirbnbAnnounceDataDto extractAirbnbData(String url) {
        log.info("- Extracting Airbnb data from URL: {}", url);

        if (!url.contains("airbnb")) {
            throw new ScrapingException(SCRAPING_INVALID_URL_ERROR);
        }

        AirbnbAnnounceDataDto airbnbData;
        try {
            log.info("- Extracting Airbnb html data from URL: {}", url);
            String htmlContent = downloadHtml(url);

            log.info("- Extract json data from html content");
            String jsonContent = extractRawJsonData(htmlContent);

            if (jsonContent == null || jsonContent.isEmpty()) {
                throw new ScrapingException(SCRAPING_INVALID_JSON_ERROR);
            }

            JsonObject rootObject = JsonParser.parseString(jsonContent).getAsJsonObject();
            JsonObject dataWrapperObject = rootObject
                    .getAsJsonArray("niobeClientData")
                    .get(0)
                    .getAsJsonArray()
                    .get(1)
                    .getAsJsonObject();

            log.info("- Converting json data to AirbnbAnnounceDataDto");
            airbnbData = GSON.fromJson(dataWrapperObject, AirbnbAnnounceDataDto.class);
        } catch (Exception e) {
            throw new ScrapingException(SCRAPING_INVALID_HTML_ERROR, e);
        }

        if (airbnbData == null) {
            throw new ScrapingException(SCRAPING_INVALID_JSON_EXTRACT_ERROR);
        }

        airbnbData
                .getData()
                .getPresentation()
                .getStayProductDetailPage()
                .getSections()
                .getSections()
                .removeIf(sectionsSub -> sectionsSub.getSection() == null);

        return airbnbData;
    }

    private String downloadHtml(String urlString) throws IOException {
        URL url = java.net.URI.create(urlString).toURL();
        HttpURLConnection connection = buildHttpConnection(url);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new ScrapingException(SCRAPING_INVALID_URL_ERROR, "responseCode", responseCode);
        }

        StringBuilder content = new StringBuilder();
        InputStream inputStream = connection.getInputStream();

        String contentEncoding = connection.getHeaderField("Content-Encoding");
        if ("gzip".equalsIgnoreCase(contentEncoding)) {
            inputStream = new GZIPInputStream(inputStream);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        connection.disconnect();
        return content.toString();
    }

    private String extractRawJsonData(String htmlContent) {
        String startMarker = "<script id=\"data-deferred-state-0\"";
        String endMarker = "</script>";

        int startIndex = htmlContent.indexOf(startMarker);
        if (startIndex == -1) {
            return null;
        }

        int jsonStart = htmlContent.indexOf(">", startIndex) + 1;
        if (jsonStart == 0) {
            return null;
        }

        int endIndex = htmlContent.indexOf(endMarker, jsonStart);
        if (endIndex == -1) {
            return null;
        }

        String rawJsonContent = htmlContent.substring(jsonStart, endIndex).trim();

        if (rawJsonContent.startsWith("{") && rawJsonContent.endsWith("}")) {
            return rawJsonContent;
        }

        return null;
    }

    private static HttpURLConnection buildHttpConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(8000);
        connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        connection.setRequestProperty(
                "Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        connection.setRequestProperty("Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Cache-Control", "max-age=0");
        connection.setRequestProperty(
                "Sec-Ch-Ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"");
        connection.setRequestProperty("Sec-Ch-Ua-Mobile", "?0");
        connection.setRequestProperty("Sec-Ch-Ua-Platform", "\"Windows\"");
        connection.setRequestProperty("Sec-Fetch-Dest", "document");
        connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
        connection.setRequestProperty("Sec-Fetch-Site", "none");
        connection.setRequestProperty("Sec-Fetch-User", "?1");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        return connection;
    }
}
