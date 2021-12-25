package com.jsw.app.service;

import com.jsw.app.entity.Member;
import com.jsw.app.exception.UserAlreadyExistException;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {

    public Member save(Member member) throws UserAlreadyExistException, IllegalStateException;
    
}