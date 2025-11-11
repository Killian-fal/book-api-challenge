package fr.killiandev.book.guide.domain.entity.guide;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeneralInfo implements Serializable {

    private String propertyName;

    private Localisation localisation;

    private String welcomeMessage;

    private Boolean showContactInfo;

    private String checkinInstructions;

    private String checkoutInstructions;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Localisation implements Serializable {
        private String address;
        private Double latitude;
        private Double longitude;
    }
}
