package com.jsw.app.service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.jsw.app.entity.Member;
import com.jsw.app.exception.UserAlreadyExistException;
import com.jsw.app.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> memberWrapper = memberRepository.findByEmail(email);
        Member member = memberWrapper.orElseThrow(() -> new UsernameNotFoundException("아이디와 비밀번호를 확인해주세요"));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
        member.setAuthorities(authorities);

        return member;
    }

    /**
    * 회원가입
    * @param member 회원가입할 Member 정보
    * @return 회원가입 완료된 Member 객체
    * @ author 정상완
    */
    @Transactional
    @Override
    public Member save(Member member) throws UserAlreadyExistException, InvalidParameterException {
        // 올바른 이메일 주소인지 체크
        if (!validateEmail(member.getEmail())) {
            log.error("No Invalid Email: {}", member.getEmail());
            throw new InvalidParameterException("올바른 이메일을 입력해주세요.");
        }

        // 존재하는 이메일인지 체크
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            log.error("Already exists Email: {}", member.getEmail());
            throw new UserAlreadyExistException("이미 존재하는 이메일입니다.");
        }

        Date now = new Date(System.currentTimeMillis());
        member.setCreatedAt(now);
        member.setUpdatedAt(now);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        log.info("Insert new Member:{}", member);
        
        return memberRepository.save(member);
    }
    
    /**
    * Email Format 체크
    * @param email 체크할 email
    * @return email validation 여부
    * @ author 정상완
    */
    private boolean validateEmail (String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
                            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern).matcher(email).matches();
    }
}