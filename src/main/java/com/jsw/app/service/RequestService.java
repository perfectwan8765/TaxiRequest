package com.jsw.app.service;

import java.util.List;

import com.jsw.app.entity.Request;
import com.jsw.app.exception.AddressInvalidException;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public interface RequestService {

    public List<Request> getRequestList();

    public Request makeRequet (String address, String header) throws AddressInvalidException, AuthenticationCredentialsNotFoundException, Exception;

    public Request acceptRequest (Long taxiRequestId, String header) throws NullPointerException, IllegalArgumentException, AuthenticationCredentialsNotFoundException, Exception;
    
}