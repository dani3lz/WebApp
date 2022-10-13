package com.endava.webapp.Controller;

import com.endava.webapp.DTO.ErrorResponse;
import com.endava.webapp.Exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ErrorResponse notFoundExceptionHandler(NotFoundException exception){
        return new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND,":(");
    }
}
