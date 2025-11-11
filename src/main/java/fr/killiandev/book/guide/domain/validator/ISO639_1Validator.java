package fr.killiandev.book.guide.domain.validator;

import fr.killiandev.book.guide.domain.util.LanguageValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISO639_1Validator implements ConstraintValidator<ISO639_1, String> {

    private boolean allowNull;
    private boolean allowEmpty;

    @Override
    public void initialize(ISO639_1 constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }

        if (value.trim().isEmpty()) {
            return allowEmpty;
        }

        return LanguageValidator.isValidISO639_1(value);
    }
}
