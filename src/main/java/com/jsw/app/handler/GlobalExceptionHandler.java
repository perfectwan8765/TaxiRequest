package com.jsw.app.handler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.jsw.app.exception.CustomException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<Map<String, String>> handleCustomException(CustomException e) {
        String errMsg = messageSource.getMessage(e.getMessageCode(), null, Locale.getDefault());
        log.error("Throw Exception!!! HTTPStatus: {}, Message: {}", e.getStatus(), errMsg);
        
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("message", errMsg);
        
        return ResponseEntity.status(e.getStatus()).body(messageMap);
    }

}