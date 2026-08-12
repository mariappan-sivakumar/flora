package com.mari.flora.exception;

import org.springframework.http.HttpStatus;

public class FunctionalException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public FunctionalException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
