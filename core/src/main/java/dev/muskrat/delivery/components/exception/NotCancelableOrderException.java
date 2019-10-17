package dev.muskrat.delivery.components.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Not cancel this order")
public class NotCancelableOrderException extends RuntimeException {

    public NotCancelableOrderException(String message) {
        super(message);
    }
}
