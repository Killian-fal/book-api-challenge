package fr.killiandev.book.observability.api.exception.type;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import fr.killiandev.book.observability.api.exception.FunctionalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthException extends FunctionalException {

    public AuthException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public AuthException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
