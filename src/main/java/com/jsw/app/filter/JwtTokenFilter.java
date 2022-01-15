package com.jsw.app.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsw.app.service.MemberService;
import com.jsw.app.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MemberService memberService;

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UserDetails authentication = null;

        try {
            String email = jwtUtil.getSubjectForHeader(request);
            authentication = memberService.loadUserByUsername(email);
        } catch (Exception e) {
            log.error("Invalid token, Check HTTP Request Header value");
            
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("message", messageSource.getMessage("err.login.needLogin", null, Locale.getDefault()));
            
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            OutputStream out = response.getOutputStream();
            out.write(mapper.writeValueAsString(resultMap).getBytes());
            out.flush();
            out.close();
        }
        
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authentication.getUsername(), null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return "/users/sign-in".equals(path); // login Url 제외
    }
    
}