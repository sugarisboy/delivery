package dev.muskrat.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class EntityExistException extends RuntimeException {

    public EntityExistException(String message) {
        super(message);
    }

}
