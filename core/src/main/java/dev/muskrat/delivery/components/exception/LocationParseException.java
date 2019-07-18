package dev.muskrat.delivery.components.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class LocationParseException extends RuntimeException {

    public LocationParseException(String message) {
        super(message);
    }
}
