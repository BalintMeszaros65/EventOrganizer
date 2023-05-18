package com.codecool.eventorganizer.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Set;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({SQLException.class, NoSuchElementException.class, IllegalArgumentException.class,
        IllegalStateException.class, ArithmeticException.class})
    @ResponseBody
    public String handleBadRequest(Throwable exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseBody
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> "Error in object '" + fieldError.getObjectName() + "' on field '"
                        + fieldError.getField() + "' : " +  fieldError.getDefaultMessage()
                )
                .collect(java.util.stream.Collectors.joining(", "));
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { ConstraintViolationException.class })
    public String handleBadConstraintRequest(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            builder.append(violation.getMessage()).append("\n");
        }
        return builder.toString();
    }
}
