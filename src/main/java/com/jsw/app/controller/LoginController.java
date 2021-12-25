package com.jsw.app.controller;

import java.security.InvalidParameterException;

import com.jsw.app.entity.Member;
import com.jsw.app.entity.MemberDTO;
import com.jsw.app.exception.UserAlreadyExistException;
import com.jsw.app.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/users/sign-up")
    public ResponseEntity signUp (Member member) {
        try {
            member = memberService.save(member);
        } catch (UserAlreadyExistException ue) {
            // 이미 가입된 이메일인 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ue.getMessage());        
        } catch (InvalidParameterException ie) {
            // 요청 파라미터가 올바르지 않은 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());        
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberDTO(member));    
    }

}