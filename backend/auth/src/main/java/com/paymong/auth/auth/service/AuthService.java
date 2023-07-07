package com.paymong.auth.auth.service;

import com.paymong.auth.auth.dto.request.LoginReqDto;
import com.paymong.auth.auth.dto.response.LoginResDto;
import com.paymong.auth.auth.dto.response.ReissueResDto;
import com.paymong.auth.auth.repository.MemberRepository;
import com.paymong.auth.auth.repository.PayPointRepository;
import com.paymong.auth.global.redis.SessionRepository;
import com.paymong.auth.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PayPointRepository payPointRepository;
    private final SessionRepository sessionRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.access_token_expired}")
    private Long accessTokenExpired;
    @Value("${jwt.refresh_token_expired}")
    private Long refreshTokenExpired;

    @Transactional
    public LoginResDto login(LoginReqDto loginReqDto) throws RuntimeException {


        return null;
    }

    public LoginResDto loginWatch(LoginReqDto loginReqDto) throws RuntimeException {
        return null;
    }

    public ReissueResDto reissue(String refreshToken) throws RuntimeException  {
        return null;
    }
}
