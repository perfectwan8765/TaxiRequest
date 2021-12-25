package com.jsw.app.entity;

import java.util.Date;

import com.jsw.app.enums.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {

    private Long id;
    private String email;
    private UserRole userType;
    private Date createdAt;
    private Date updatedAt;

    public MemberDTO (Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.userType = member.getUserType();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }
     
}