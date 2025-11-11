package fr.killiandev.book.observability.api.exception.type;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import fr.killiandev.book.observability.api.exception.FunctionalException;

public class SupabaseException extends FunctionalException {

    public SupabaseException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public SupabaseException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
