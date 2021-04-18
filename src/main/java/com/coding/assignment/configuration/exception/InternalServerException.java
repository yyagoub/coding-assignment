package com.coding.assignment.configuration.exception;

public class InternalServerException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InternalServerException() {
        super("INTERNAL_SERVER_ERROR");
    }

}