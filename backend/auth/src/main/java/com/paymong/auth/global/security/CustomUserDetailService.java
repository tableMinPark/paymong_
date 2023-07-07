package com.paymong.auth.global.security;

import com.paymong.auth.auth.entity.Member;
import com.paymong.auth.auth.entity.Role;
import com.paymong.auth.auth.repository.MemberRepository;
import com.paymong.auth.auth.repository.RoleRepository;
import com.paymong.auth.global.code.AuthFailCode;
import com.paymong.core.exception.failException.NotFoundFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) {
        try {
            Member member = memberRepository.findById(Long.parseLong(memberId))
                    .orElseThrow(() ->  new NotFoundFailException(AuthFailCode.NOT_FOUND_MEMBER));

            List<Role> roles = roleRepository.findByMemberId(member.getMemberId());
            return CustomUserDetail.of(member, roles);
        } catch (RuntimeException e) {
            throw new NotFoundFailException(AuthFailCode.NOT_FOUND_MEMBER);
        }
    }
}
