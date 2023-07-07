package com.paymong.auth.global.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.paymong.auth.auth.entity.Member;
import com.paymong.auth.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetail implements UserDetails {
    private String memberId;
    private String password;
    private List<String> roles = new ArrayList<>();    //권한 목록

    public static UserDetails of(Member member, List<Role> roleList) {
        String memberId = member.getMemberId().toString();
        String password = member.getPassword();
        List<String> roles = roleList.stream()
                .map((role) -> role.getCode().getName())
                .collect(Collectors.toList());

        return CustomUserDetail.builder()
            .memberId(memberId)
            .password(password)
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
        return password;
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
