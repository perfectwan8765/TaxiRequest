package com.jsw.app.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jsw.app.properties.JwtConfigProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Autowired
    private JwtConfigProperty property;
    
    /**
    * JWT 기반 Token 생성
    * @param subject Token 생성할 Subject(Member Email)
    * @return JWT Token
    * @ author 정상완
    */
    public String createToken (String subject) {
        return JWT.create()
            .withSubject(subject)
            .withExpiresAt(new Date(System.currentTimeMillis() + property.getExpireTime()))
            .sign(Algorithm.HMAC512(property.getSecretKey().getBytes()));
    }

    /**
    * HTTP Header에서 JWT Token Subject 추출
    * @param header HTTP Request Header(Authorization)
    * @return JWT Token Subject
    * @ author 정상완
    */
    public String getSubjectForHeader (String header) {
        return JWT.require(Algorithm.HMAC512(property.getSecretKey().getBytes()))
            .build()
            .verify(header.replace(property.getPrefix() + " ", ""))
            .getSubject();
    }
    
}