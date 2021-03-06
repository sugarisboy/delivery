package dev.muskrat.delivery.components.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Location not parse")
public class LocationParseException extends RuntimeException {

    public LocationParseException(String message) {
        super(message);
    }
}
