package com.jsw.app.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private HttpStatus status;

    public CustomException (HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus () {
        return this.status;
    }

    public void setStatus (HttpStatus status) {
        this.status = status;
    }
    
}