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
public class UpdateGuideDto {

    @NotNull
    private Long guideId;

    @Valid
    @NotNull
    private UpdateGuideDtoGuide guide;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoGuide {
        @Valid
        private UpdateGuideDtoGeneralInfo generalInfo;

        @Valid
        private UpdateGuideDtoWifiInfo wifiInfo;

        @Valid
        private UpdateGuideDtoRulesInfo rulesInfo;

        @Valid
        private UpdateGuideDtoTransportationInfo transportationInfo;

        private List<@Valid UpdateGuideDtoRestaurant> restaurants;
        private List<@Valid UpdateGuideDtoAttraction> attractions;
        private List<@Valid UpdateGuideDtoEmergencyContact> emergencyContacts;
        private List<@Valid UpdateGuideDtoApplianceInstruction> applianceInstructions;
        private List<@Valid UpdateGuideDtoCustomCategory> customCategories;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoGeneralInfo {
        @NotEmpty
        private String propertyName;

        @NotNull
        @Valid
        private UpdateGuideDtoLocalisation localisation;

        private String welcomeMessage;
        private Boolean showContactInfo;
        private String checkinInstructions;
        private String checkoutInstructions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoLocalisation {
        @NotEmpty
        private String address;

        @NotNull
        private Double latitude;

        @NotNull
        private Double longitude;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoRulesInfo {
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
    public static class UpdateGuideDtoTransportationInfo {
        private Boolean parkingAvailable;
        private String parkingInfo;
        private String publicTransportInfo;
        private String taxiInfo;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoWifiInfo {
        private String networkName;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoApplianceInstruction {
        private String name;
        private String instructions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoAttraction {
        private String name;
        private String description;
        private String address;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoRestaurant {
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
    public static class UpdateGuideDtoCustomCategory {
        private String name;
        private List<@Valid UpdateGuideDtoCustomItem> items;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoCustomItem {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGuideDtoEmergencyContact {
        private String name;
        private String phoneNumber;
    }
}
