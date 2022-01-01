package com.jsw.app.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private HttpStatus status;
    private String messageCode;

    public CustomException (HttpStatus status, String messageCode) {
        this.status = status;
        this.messageCode = messageCode;
    }

    public HttpStatus getStatus () {
        return this.status;
    }

    public void setStatus (HttpStatus status) {
        this.status = status;
    }

    public String getMessageCode() {
        return this.messageCode;
    }
    
}