package fr.killiandev.book.observability.api.exception.type;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import fr.killiandev.book.observability.api.exception.FunctionalException;

public class GuideException extends FunctionalException {

    public GuideException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public GuideException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
