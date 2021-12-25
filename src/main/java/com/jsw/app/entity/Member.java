package com.jsw.app.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.jsw.app.converter.RoleConverter;
import com.jsw.app.enums.UserRole;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="MEMBER")
@SequenceGenerator (
    name = "MEMBER_ID_GENERATOR",
    sequenceName = "MEMBER_SEQ",
    initialValue = 1,
    allocationSize = 1 // default가 50임. 사용할때 조심!!!
)
public class Member implements UserDetails {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_ID_GENERATOR" )
    private Long id;

    @Column(name="EMAIL", nullable=false, length=255)
    private String email;

    @Column(name="PASSWORD", nullable=false)
    private String password;

    @Column(name="USER_TYPE", nullable=false)
    @Convert(converter = RoleConverter.class)
    private UserRole userType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_AT")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATED_AT")
    private Date updatedAt;

    @Transient
    private Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
     
}