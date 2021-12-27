package com.jsw.app.controller;

import com.jsw.app.entity.Member;
import com.jsw.app.entity.MemberDTO;
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
    public ResponseEntity<MemberDTO> signUp (Member member) {
        member = memberService.save(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberDTO(member));    
    }

}