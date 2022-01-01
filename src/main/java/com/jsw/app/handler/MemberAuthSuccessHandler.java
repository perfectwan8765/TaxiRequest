package com.jsw.app.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsw.app.properties.JwtConfigProperty;
import com.jsw.app.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtConfigProperty jwtConfigProperty;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // create Token
        String token = jwtUtil.createToken(authentication.getName());

        // Add token in response
        response.addHeader("Authorization", String.format("%s %s", jwtConfigProperty.getPrefix(), token));

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("accessToken", token);

        response.setStatus(HttpStatus.OK.value()); // 200
        response.setContentType("applicatoin/json");

        OutputStream out = response.getOutputStream();
        out.write(mapper.writeValueAsString(resultMap).getBytes());
        out.flush();
        out.close();
    }

}