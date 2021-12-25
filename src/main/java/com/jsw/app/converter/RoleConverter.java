package com.jsw.app.converter;

import java.util.EnumSet;
import java.util.NoSuchElementException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.jsw.app.enums.UserRole;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole userRole) {
        return userRole.getAuthority();
    }

    @Override
    public UserRole convertToEntityAttribute(String code) {
        return EnumSet.allOf(UserRole.class).stream()
                      .filter(e -> e.getAuthority().equals(code))
                      .findAny()
                      .orElseThrow(() -> new NoSuchElementException());
    }

}