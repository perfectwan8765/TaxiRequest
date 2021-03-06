package com.jsw.app.service;

import java.util.List;

import com.jsw.app.entity.Request;
import com.jsw.app.exception.CustomException;

public interface RequestService {

    public List<Request> getRequestList();

    public Request makeRequet (String address) throws CustomException;

    public Request acceptRequest (Long taxiRequestId) throws CustomException;
    
}