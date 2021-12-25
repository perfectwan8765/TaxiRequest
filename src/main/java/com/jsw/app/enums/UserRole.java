package com.jsw.app.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    DRIVER("D", "driver"),
    PASSENGER("P", "passenger");

    private UserRole (String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    private String roleName;
    private String description;

    @Override
    public String getAuthority() {
        return this.roleName;
    }

    public String getDescription()  {
        return this.description;
    }
    
}
