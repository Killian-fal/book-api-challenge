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
public class AIGuideDto {

    @Valid
    @NotNull
    private AIGuideDtoGuide guide;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AIGuideDtoGuide {
        @NotEmpty
        private String language;

        @Valid
        private AIGuideDtoGeneralInfo generalInfo;

        @Valid
        private AIGuideDtoRulesInfo rulesInfo;

        @Valid
        private AIGuideDtoTransportationInfo transportationInfo;

        private List<@Valid AIGuideDtoRestaurant> restaurants;
        private List<@Valid AIGuideDtoAttraction> attractions;
        private List<@Valid AIGuideDtoEmergencyContact> emergencyContacts;
        private List<@Valid AIGuideDtoApplianceInstruction> applianceInstructions;
        private List<@Valid AIGuideDtoCustomCategory> customCategories;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AIGuideDtoGeneralInfo {
        private String propertyName;
        private String welcomeMessage;
        private Boolean showContactInfo;
        private String checkinInstructions;
        private String checkoutInstructions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AIGuideDtoRulesInfo {
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
    public static class AIGuideDtoTransportationInfo {
        private Boolean parkingAvailable;
        private String parkingInfo;
        private String publicTransportInfo;
        private String taxiInfo;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AIGuideDtoApplianceInstruction {
        private String name;
        private String instructions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AIGuideDtoAttraction {
        private String name;
        private String description;
        private String address;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AIGuideDtoRestaurant {
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
    public static class AIGuideDtoCustomCategory {
        private String name;
        private List<@Valid AIGuideDtoCustomItem> items;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AIGuideDtoCustomItem {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AIGuideDtoEmergencyContact {
        private String name;
        private String phoneNumber;
    }
}
