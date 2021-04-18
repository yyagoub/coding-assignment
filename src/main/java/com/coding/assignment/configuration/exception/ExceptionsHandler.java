package com.coding.assignment.configuration.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    // let's see if we can delete all overridden methods and replace them with one
    // or two custom handler.

    // 401 - UNAUTHORIZED UnauthorizedException
    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(UNAUTHORIZED, ex.getMessage(), ex);
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
    }

    // 422 - UNPROCESSABLE_ENTITY UnprocessableEntityException
    @ExceptionHandler(UnprocessableEntityException.class)
    protected ResponseEntity<?> handleUnprocessableEntityException(UnprocessableEntityException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(UNPROCESSABLE_ENTITY, ex.getMessage(), ex, ex.getErrors());
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
    }

    // 500 - INTERNAL_SERVER_ERROR InternalServerException
    @ExceptionHandler(InternalServerException.class)
    protected ResponseEntity<?> handleInternalServerException(InternalServerException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
    }

}