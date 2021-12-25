package com.jsw.app.converter;

import java.util.EnumSet;
import java.util.NoSuchElementException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.jsw.app.enums.RequestStatus;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<RequestStatus, String> {

    @Override
    public String convertToDatabaseColumn(RequestStatus status) {
        return status.getStatus();
    }

    @Override
    public RequestStatus convertToEntityAttribute(String code) {
        return EnumSet.allOf(RequestStatus.class).stream()
                      .filter(s -> s.getStatus().equals(code))
                      .findAny()
                      .orElseThrow(() -> new NoSuchElementException());
    }

}