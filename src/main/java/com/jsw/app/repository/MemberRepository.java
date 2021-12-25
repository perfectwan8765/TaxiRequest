package com.jsw.app.repository;

import java.util.Optional;

import com.jsw.app.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    
}
