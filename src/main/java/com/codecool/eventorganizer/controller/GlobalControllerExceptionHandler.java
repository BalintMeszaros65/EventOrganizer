package com.codecool.eventorganizer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({SQLException.class, NoSuchElementException.class, IllegalArgumentException.class,
        IllegalStateException.class, ArithmeticException.class})
    @ResponseBody
    public String handleBadRequest(Throwable exception) {
        return exception.getMessage();
    }

}
