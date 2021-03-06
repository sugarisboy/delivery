package dev.muskrat.delivery.components.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Entity exists exception")
public class EntityExistException extends RuntimeException {

    public EntityExistException(String message) {
        super(message);
    }

}
