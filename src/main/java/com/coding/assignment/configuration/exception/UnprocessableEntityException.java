package com.coding.assignment.configuration.exception;

import java.util.List;

public class UnprocessableEntityException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    List<ValidationError> errors;

    public UnprocessableEntityException(List<ValidationError> errors) {
        super("UNPROCESSABLE_ENTITY");
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

}