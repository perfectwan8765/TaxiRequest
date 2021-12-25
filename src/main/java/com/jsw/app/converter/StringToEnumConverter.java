package com.jsw.app.converter;

import com.jsw.app.enums.UserRole;

import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, UserRole> {

    @Override
    public UserRole convert (String source) {
        return UserRole.valueOf(source.toUpperCase());
    }

}