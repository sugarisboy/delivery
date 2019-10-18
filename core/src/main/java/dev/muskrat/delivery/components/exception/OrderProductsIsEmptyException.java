package dev.muskrat.delivery.components.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Order products after update is empty, use update status for cancel order")
public class OrderProductsIsEmptyException extends RuntimeException {

    public OrderProductsIsEmptyException() {

    }

    public OrderProductsIsEmptyException(String message) {
        super(message);
    }
}
