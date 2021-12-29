package com.jsw.app.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MessageSource messageSource;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("message", messageSource.getMessage("err.login.infoCheck", null, Locale.getDefault()));

        response.setStatus(HttpStatus.BAD_REQUEST.value()); // 400
        response.setContentType("applicatoin/json");

        OutputStream out = response.getOutputStream();
        out.write(mapper.writeValueAsString(resultMap).getBytes());
        out.flush();
        out.close();
    }

}