package dev.muskrat.delivery.components.exception;

import dev.muskrat.delivery.validations.dto.ValidationExceptionDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.AbstractPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class EntityValidatorExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        ValidationExceptionDTO exception = null;

        for (Object obj : ex.getBindingResult().getModel().values()) {
            if (obj instanceof AbstractPropertyBindingResult) {
                AbstractPropertyBindingResult result = (AbstractPropertyBindingResult) obj;
                ObjectError objError = result.getAllErrors().get(0);
                if (objError instanceof FieldError) {
                    FieldError error = (FieldError) objError;
                    String message = error.getDefaultMessage();
                    String field = error.getField();
                    exception = ValidationExceptionDTO.builder()
                        .id(0)
                        .message(message)
                        .field(field)
                        .build();
                } else {
                    ObjectError error = objError;
                    String message = error.getDefaultMessage();
                    exception = ValidationExceptionDTO.builder()
                        .id(0)
                        .message(message)
                        .field("dto")
                        .build();
                }
            }
        }

        return handleExceptionInternal(ex, exception, headers, status, request);
    }
}
