package fr.killiandev.book.observability.api.exception.type;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import fr.killiandev.book.observability.api.exception.FunctionalException;

public class MapException extends FunctionalException {

    public MapException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public MapException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
