package com.paymong.global.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetail implements UserDetails {
    private String memberId;
    private String internalToken;
    private List<String> roles;

    public static UserDetails of(String memberId, String internalToken, List<String> roles) {
        return CustomUserDetail.builder()
            .memberId(memberId)
            .internalToken(internalToken)
            .roles(roles)
            .build();
    }

    /** 멤버 id **/
    @Override
    public String getUsername() {
        return memberId;
    }
    /** 비밀번호 **/
    @Override
    public String getPassword() {
        return internalToken;
    }
    /** 권한 목록 **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return false;
    }
}
