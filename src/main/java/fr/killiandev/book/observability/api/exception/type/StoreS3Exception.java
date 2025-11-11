package fr.killiandev.book.observability.api.exception.type;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import fr.killiandev.book.observability.api.exception.FunctionalException;

public class StoreS3Exception extends FunctionalException {

    public StoreS3Exception(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public StoreS3Exception(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
