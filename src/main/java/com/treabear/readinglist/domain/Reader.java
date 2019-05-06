package com.treabear.readinglist.domain;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reader
 * 
 * UserDetails를 구현하므로써, 스프링 시큐리티에서 사용자를 표현하는 객체로 Reader를 사용할 수 있다.
 */
@Entity
@ToString
@Getter @Setter
public class Reader implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id // Reader 필드
    private String username;
    private String fullname;
    private String password;

    // UserDetails Methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 항상 READER 권한 부여
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_READER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료X 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠김X 여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격의 유효X 여부
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부
        return true;
    }


    
}