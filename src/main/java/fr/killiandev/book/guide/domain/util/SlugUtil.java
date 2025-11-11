package fr.killiandev.book.guide.domain.util;

import static fr.killiandev.book.observability.api.exception.ExceptionType.GUIDE_INVALID_SLUG_ERROR;

import fr.killiandev.book.guide.domain.entity.Guide;
import fr.killiandev.book.observability.api.exception.type.GuideException;
import java.text.Normalizer;
import java.util.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SlugUtil {

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            throw new GuideException(GUIDE_INVALID_SLUG_ERROR);
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return withoutAccents
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
    }

    public static String formatSlug(Guide guide) {
        return "%s-%s"
                .formatted(System.currentTimeMillis(), guide.getGeneralInfo().getPropertyName());
    }
}
