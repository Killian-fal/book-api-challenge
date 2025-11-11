package fr.killiandev.book.guide.domain.dto;

import fr.killiandev.book.guide.domain.validator.ISO639_1;
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
public class CreateGuideDto {

    @Valid
    @NotNull
    private CreateGuideDtoGuide guide;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideDtoGuide {
        @ISO639_1
        @NotEmpty
        private String language;

        @Valid
        private CreateGuideDtoGeneralInfo generalInfo;

        @Valid
        private CreateGuideDtoWifiInfo wifiInfo;

        @Valid
        private CreateGuideDtoRulesInfo rulesInfo;

        @Valid
        private CreateGuideDtoTransportationInfo transportationInfo;

        private List<@Valid CreateGuideDtoRestaurant> restaurants;
        private List<@Valid CreateGuideDtoAttraction> attractions;
        private List<@Valid CreateGuideDtoEmergencyContact> emergencyContacts;
        private List<@Valid CreateGuideDtoApplianceInstruction> applianceInstructions;
        private List<@Valid CreateGuideDtoCustomCategory> customCategories;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideDtoGeneralInfo {
        @NotEmpty
        private String propertyName;

        @NotNull
        @Valid
        private CreateGuideDtoLocalisation localisation;

        private String welcomeMessage;
        private Boolean showContactInfo;
        private String checkinInstructions;
        private String checkoutInstructions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideDtoLocalisation {
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
    public static class CreateGuideDtoRulesInfo {
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
    public static class CreateGuideDtoTransportationInfo {
        private Boolean parkingAvailable;
        private String parkingInfo;
        private String publicTransportInfo;
        private String taxiInfo;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideDtoWifiInfo {
        private String networkName;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideDtoApplianceInstruction {
        private String name;
        private String instructions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideDtoAttraction {
        private String name;
        private String description;
        private String address;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideDtoRestaurant {
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
    public static class CreateGuideDtoCustomCategory {
        private String name;
        private List<@Valid CreateGuideDtoCustomItem> items;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideDtoCustomItem {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateGuideDtoEmergencyContact {
        private String name;
        private String phoneNumber;
    }
}
