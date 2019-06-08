package dev.muskrat.delivery.exception;

import dev.muskrat.delivery.dto.ValidationExceptionDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
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
            if (obj instanceof BeanPropertyBindingResult) {
                BeanPropertyBindingResult result = (BeanPropertyBindingResult) obj;
                FieldError error = (FieldError) result.getAllErrors().get(0);

                String message = error.getDefaultMessage();
                String field = error.getField();

                /*String data[] = message.split(":");
                if (data.length == 2) {
                    exception = ValidationExceptionDTO.builder()
                        .id(Integer.valueOf(data[0]))
                        .message(data[1])
                        .field(field)
                        .build();
                } else {*/
                exception = ValidationExceptionDTO.builder()
                    .id(0)
                    .message(message)
                    .field(field)
                    .build();
                //}
            }
        }

        return handleExceptionInternal(ex, exception, headers, status, request);
    }
}
