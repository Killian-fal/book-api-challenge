package fr.killiandev.book.scraping.domain;

import com.nimbusds.jose.shaded.gson.*;
import fr.killiandev.book.scraping.domain.dto.AirbnbAnnounceDataDto;
import java.lang.reflect.Type;

public class SectionDeserializer implements JsonDeserializer<AirbnbAnnounceDataDto.SectionItem> {
    @Override
    public AirbnbAnnounceDataDto.SectionItem deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        String typename = obj.get("__typename").getAsString();

        return switch (typename) {
            case "PhotoTourModalSection" -> context.deserialize(obj, AirbnbAnnounceDataDto.PhotoTourModalSection.class);
            case "LocationSection" -> context.deserialize(obj, AirbnbAnnounceDataDto.LocationSection.class);
            case "PoliciesSection" -> context.deserialize(obj, AirbnbAnnounceDataDto.PoliciesSection.class);
            case "PdpHighlightsSection" -> context.deserialize(obj, AirbnbAnnounceDataDto.PdpHighlightsSection.class);
            case "PdpDescriptionSection" -> context.deserialize(obj, AirbnbAnnounceDataDto.PdpDescriptionSection.class);
            case "AmenitiesSection" -> context.deserialize(obj, AirbnbAnnounceDataDto.AmenitiesSection.class);
            case "GeneralListContentSection" -> context.deserialize(
                    obj, AirbnbAnnounceDataDto.GeneralListContentSection.class);
            default -> null;
        };
    }
}
