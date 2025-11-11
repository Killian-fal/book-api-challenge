package fr.killiandev.book.guide.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetGuideDto {

    @Valid
    @NotNull
    private GetGuideDtoGuide guide;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoGuide {
        @NotNull
        private Long id;

        @NotEmpty
        private String language;

        @NotEmpty
        private String slug;

        @Valid
        private GetGuideDtoGeneralInfo generalInfo;

        @Valid
        private GetGuideDtoWifiInfo wifiInfo;

        @Valid
        private GetGuideDtoRulesInfo rulesInfo;

        @Valid
        private GetGuideDtoTransportationInfo transportationInfo;

        private List<@Valid GetGuideDtoRestaurant> restaurants;
        private List<@Valid GetGuideDtoAttraction> attractions;
        private List<@Valid GetGuideDtoEmergencyContact> emergencyContacts;
        private List<@Valid GetGuideDtoApplianceInstruction> applianceInstructions;
        private List<@Valid GetGuideDtoCustomCategory> customCategories;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoGeneralInfo {
        private String propertyName;
        private GetGuideDtoLocalisation localisation;
        private String welcomeMessage;
        private Boolean showContactInfo;
        private String checkinInstructions;
        private String checkoutInstructions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoLocalisation {
        private String address;
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoRulesInfo {
        private String quietHours;
        private Boolean smokingAllowed;
        private String smokingPolicy;
        private Boolean petsAllowed;
        private String petsPolicy;
        private Boolean partiesAllowed;
        private String partiesPolicy;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoTransportationInfo {
        private Boolean parkingAvailable;
        private String parkingInfo;
        private String publicTransportInfo;
        private String taxiInfo;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoWifiInfo {
        private String networkName;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoApplianceInstruction {
        private String name;
        private String instructions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoAttraction {
        private String name;
        private String description;
        private String address;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoRestaurant {
        private String name;
        private String description;
        private String address;
        private Double latitude;
        private Double longitude;
        private String googleLink;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoCustomCategory {
        private String name;
        private List<@Valid GetGuideDtoCustomItem> items;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoCustomItem {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetGuideDtoEmergencyContact {
        private String name;
        private String phoneNumber;
    }
}
