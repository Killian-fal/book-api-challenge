package fr.killiandev.book.guide.domain;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GuideConstant {

    public static final String TRANSLATE_PROMPT =
            """
            There is a guide of a location. Translate it in %s (ISO 639-1). Don't forget to translate the welcome message. JSON representation of the guide : %s
            """;

    public static final String CREATE_PROMPT =
            """
            Create a guide for a location based on the following information : %s. The guide must be in %s (ISO 639-1). You must not use dates; the guide should be readable at any time.
            For the "propertyName" field, use the name of the location if it is provided. If not, use a general name like "Cozy apartment in [City]" or "Charming house in [City]". This field is MANDATORY.
            For the "welcomeMessage" field, create a warm and inviting message that reflects the unique characteristics of the location. This field is MANDATORY.
            For the "emergencyContacts" section, if you don't have specific numbers for the location, use general emergency numbers of the country of the location provided.
            For the "attractions" section, you must provide at least 3 recommendations. If you don't have specific recommendations for the location, use general recommendations of the city of the location provided.
            For the "applianceInstructions" section, complete only if there are information about appliances in the provided information. If there are no such details, leave this section empty.
            => Example:
            1. Smart TV; Use the Samsung remote to turn on. Netflix and Hulu are already logged in. Press the 'Source' button to switch between apps.
            2. Thermostat; Digital thermostat on the wall by the kitchen. Set between 68-72°F for comfort. Press 'Mode' to switch between heat and cool.
            For the "customCategories" section, create custom categories only if there are relevant details in the provided information. If not, leave this section empty.
            For "generalInfo" section (fill it only if there are relevant details in the provided information. If not, leave this section empty), there is example for "checkinInstructions" and "checkoutInstructions" field :
            - "checkinInstructions": Check that the garage keys are in the basket at the entrance.
            - "checkoutInstructions": Throw away the trash
            """;

    public static final String GOOGLE_MAPS_SEARCH_URL_TEMPLATE =
            "https://www.google.com/maps/search/?api=1&query=%s&query_place_id=%s";
}
