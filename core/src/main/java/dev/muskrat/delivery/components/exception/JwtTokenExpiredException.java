package dev.muskrat.delivery.components.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Token is expired")
public class JwtTokenExpiredException extends AccessDeniedException {

    public JwtTokenExpiredException(String msg) {
        super(msg);
    }
}
