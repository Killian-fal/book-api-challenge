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
public class GuideCreatedAiDto {

    private GeneralInfo generalInfo;

    private RulesInfo rulesInfo;

    private TransportationInfo transportationInfo;

    private List<Attraction> attractions;
    private List<EmergencyContact> emergencyContacts;
    private List<ApplianceInstruction> applianceInstructions;
    private List<CustomCategory> customCategories;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneralInfo {
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
    public static class RulesInfo {
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
    public static class TransportationInfo {
        private Boolean parkingAvailable;
        private String parkingInfo;
        private String publicTransportInfo;
        private String taxiInfo;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplianceInstruction {
        private String name;
        private String instructions;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attraction {
        private String name;
        private String description;
        private String address;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomCategory {
        private String name;
        private List<CustomItem> items;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomItem {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmergencyContact {
        private String name;
        private String phoneNumber;
    }
}
