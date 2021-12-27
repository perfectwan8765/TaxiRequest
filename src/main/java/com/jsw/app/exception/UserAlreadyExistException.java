package com.jsw.app.exception;

import org.springframework.security.core.AuthenticationException;

@Deprecated
public class UserAlreadyExistException extends AuthenticationException {
    
    public UserAlreadyExistException(final String msg) {
        super(msg);
    }
}