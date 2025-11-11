package fr.killiandev.book.guide.domain.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LanguageValidator {

    private static final Set<String> ISO_639_1_CODES = new HashSet<>(Arrays.asList(Locale.getISOLanguages()));

    public static boolean isValidISO639_1(String languageCode) {
        if (languageCode == null || languageCode.isEmpty()) {
            return false;
        }

        if (languageCode.length() != 2) {
            return false;
        }

        return ISO_639_1_CODES.contains(languageCode);
    }
}
