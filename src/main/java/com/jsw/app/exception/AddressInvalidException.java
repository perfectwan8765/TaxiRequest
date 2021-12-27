package com.jsw.app.exception;

@Deprecated
public class AddressInvalidException extends IllegalArgumentException {
    
    public AddressInvalidException(final String msg) {
        super(msg);
    }
}