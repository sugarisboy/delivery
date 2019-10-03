package dev.muskrat.delivery.components.exception;

import dev.muskrat.delivery.components.exception.dto.RuntimeExceptionDTO;
import dev.muskrat.delivery.validations.dto.ValidationExceptionDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.AbstractPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class ControllerAdviceManager extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> commence(HttpServletRequest request,
                                           HttpServletResponse response,
                                           AccessDeniedException accessDeniedException) throws IOException {

        StackTraceElement[] stackTrace = accessDeniedException.getStackTrace();
        List<StackTraceElement> collect = Arrays.stream(stackTrace)
            .filter(trace -> trace.getClassName().contains("dev.muskrat"))
            .collect(Collectors.toList());

        RuntimeExceptionDTO jwtAccessDeniedExceptionDTO;
        if (collect.size() != 0) {
            StackTraceElement stackTraceElement = collect.get(0);
            jwtAccessDeniedExceptionDTO = RuntimeExceptionDTO.builder()
                .message(accessDeniedException.getMessage())
                .clazz(stackTraceElement.getClassName())
                .method(stackTraceElement.getMethodName())
                .build();
        } else {
            jwtAccessDeniedExceptionDTO = RuntimeExceptionDTO.builder()
                .message(accessDeniedException.getMessage())
                .build();
        }

        return ResponseEntity
            .status(HttpServletResponse.SC_FORBIDDEN)
            .contentType(MediaType.APPLICATION_JSON)
            .body(jwtAccessDeniedExceptionDTO);
    }

    @ExceptionHandler(value = {AuthenticationCredentialsNotFoundException.class})
    public ResponseEntity<Object> commence(AuthenticationCredentialsNotFoundException ex) {

        StackTraceElement[] stackTrace = ex.getStackTrace();
        List<StackTraceElement> collect = Arrays.stream(stackTrace)
            .filter(trace -> trace.getClassName().contains("dev.muskrat"))
            .collect(Collectors.toList());

        RuntimeExceptionDTO jwtAccessDeniedExceptionDTO;
        if (collect.size() != 0) {
            StackTraceElement stackTraceElement = collect.get(0);
            jwtAccessDeniedExceptionDTO = RuntimeExceptionDTO.builder()
                .message("Access denied: only authorized")
                .clazz(stackTraceElement.getClassName())
                .method(stackTraceElement.getMethodName())
                .build();
        } else {
            jwtAccessDeniedExceptionDTO = RuntimeExceptionDTO.builder()
                .message("Access denied: only authorized")
                .build();
        }

        return ResponseEntity
            .status(HttpServletResponse.SC_FORBIDDEN)
            .contentType(MediaType.APPLICATION_JSON)
            .body(jwtAccessDeniedExceptionDTO);
    }

    @ExceptionHandler(value = {EntityExistException.class})
    public ResponseEntity<Object> commence(RuntimeException ex) {

        StackTraceElement[] stackTrace = ex.getStackTrace();
        List<StackTraceElement> collect = Arrays.stream(stackTrace)
            .filter(trace -> trace.getClassName().contains("dev.muskrat"))
            .collect(Collectors.toList());
        StackTraceElement stackTraceElement = collect.get(0);


        RuntimeExceptionDTO exceptionDTO = RuntimeExceptionDTO.builder()
            .message(ex.getMessage())
            .clazz(stackTraceElement.getClassName())
            .method(stackTraceElement.getMethodName())
            .build();

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(exceptionDTO);
    }
}
