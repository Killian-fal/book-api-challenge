package fr.killiandev.book.observability.api.exception.type;

import fr.killiandev.book.observability.api.exception.ExceptionType;
import fr.killiandev.book.observability.api.exception.FunctionalException;

public class QuestionException extends FunctionalException {

    public QuestionException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public QuestionException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
