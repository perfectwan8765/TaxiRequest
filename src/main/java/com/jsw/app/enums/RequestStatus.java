package com.jsw.app.enums;

public enum RequestStatus {

    REQUEST("R", "request"),
    ACCEPT("A", "accepted");

    private RequestStatus (String statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }

    private String statusCode;
    private String status;

    public String getStatusCode()  {
        return this.statusCode;
    }

    public String getStatus()  {
        return this.status;
    }
    
}