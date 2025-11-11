package fr.killiandev.book.observability.api.exception.type;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import fr.killiandev.book.observability.api.exception.FunctionalException;

public class ScrapingException extends FunctionalException {

    public ScrapingException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public ScrapingException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
