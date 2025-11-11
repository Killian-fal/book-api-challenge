package fr.killiandev.book.observability.api.exception.type;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import fr.killiandev.book.observability.api.exception.FunctionalException;

public class NotFoundException extends FunctionalException {

    public NotFoundException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public NotFoundException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
