package com.jsw.app.exception;

public class AddressInvalidException extends IllegalArgumentException {
    
    public AddressInvalidException(final String msg) {
        super(msg);
    }
}