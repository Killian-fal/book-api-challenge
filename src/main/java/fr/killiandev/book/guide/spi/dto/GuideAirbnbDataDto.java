package fr.killiandev.book.guide.spi.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GuideAirbnbDataDto {

    private Data data;
    private Variables variables;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private Presentation presentation;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Presentation {
        private StayProductDetailPage stayProductDetailPage;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StayProductDetailPage {
        private SectionsMain sections;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SectionsMain {
        private List<SectionsSub> sections;
        private Metadata metadata;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Metadata {
        private MetadataSharingConfig sharingConfig;
        private SeoFeatures seoFeatures;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SeoFeatures {
        private String metaDescription;
        private String title;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetadataSharingConfig {
        private String title;
        private String location;
        private String propertyType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SectionsSub {
        private SectionItem section;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public abstract static class SectionItem {}

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneralListContentSection extends SectionItem {
        private List<GeneralListContentSectionItem> items;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneralListContentSectionItem {
        private HtmlDescription html;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AmenitiesSection extends SectionItem {
        private List<AmenitiesGroup> previewAmenitiesGroups;
        private List<AmenitiesGroup> seeAllAmenitiesGroups;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AmenitiesGroup {
        private String title;
        private List<Amenity> amenities;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Amenity {
        private Boolean available;
        private String title;
        private String subtitle;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PdpDescriptionSection extends SectionItem {
        private String title;
        private HtmlDescription htmlDescription;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HtmlDescription {
        private String htmlText;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PdpHighlightsSection extends SectionItem {
        private List<Highlights> highlights;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Highlights {
        private String title;
        private String subtitle;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PoliciesSection extends SectionItem {
        private List<HouseRule> houseRules;
        private List<HouseRule> previewSafetyAndProperties;
        private List<HouseRulesSections> houseRulesSections;
        private List<HouseRulesSections> safetyAndPropertiesSections;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HouseRule {
        private String title;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HouseRulesSections {
        private String title;
        private List<HouseRulesSectionsItem> items;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HouseRulesSectionsItem {
        private String title;
        private String subtitle;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LocationSection extends SectionItem {
        private String title;
        private String subtitle;
        private Double lat;
        private Double lng;
        private String address;
        private String addressTitle;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhotoTourModalSection extends SectionItem {
        private ShareSave shareSave;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShareSave {
        private EmbedData embedData;
        private SharingConfig sharingConfig;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmbedData {
        private String name;
        private Integer personCapacity;
        private String propertyType;
        private Integer reviewCount;
        private Double starRating;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SharingConfig {
        private String title;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Variables {
        private PdpSectionsRequest pdpSectionsRequest;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PdpSectionsRequest {
        private String adults;
        private String children;
        private Integer pets;
    }
}
