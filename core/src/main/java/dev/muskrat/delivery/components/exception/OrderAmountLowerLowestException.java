package dev.muskrat.delivery.components.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Order amount lower lowest")
public class OrderAmountLowerLowestException extends RuntimeException {

    public OrderAmountLowerLowestException(String message) {
        super(message);
    }
}