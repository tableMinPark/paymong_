package com.paymong.auth.auth.service;

import com.paymong.auth.auth.dto.request.LoginReqDto;
import com.paymong.auth.auth.dto.response.LoginResDto;
import com.paymong.auth.auth.dto.response.ReissueResDto;
import com.paymong.auth.auth.entity.Member;
import com.paymong.auth.auth.entity.PayPoint;
import com.paymong.auth.auth.repository.MemberRepository;
import com.paymong.auth.auth.repository.PayPointRepository;
import com.paymong.auth.global.code.AuthFailCode;
import com.paymong.auth.global.redis.Access;
import com.paymong.auth.global.redis.Refresh;
import com.paymong.auth.global.redis.Session;
import com.paymong.auth.global.redis.SessionRepository;
import com.paymong.auth.global.security.Token;
import com.paymong.auth.global.security.TokenProvider;
import com.paymong.core.code.DeviceCode;
import com.paymong.core.exception.failException.InvalidFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.UUID;

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
    public LoginResDto login(LoginReqDto loginReqDto, DeviceCode deviceCode) throws RuntimeException {
        String playerId = loginReqDto.getPlayerId();

        /* 플레이어 아이디 형식 확인 */
        if (!StringUtils.hasText(playerId) || !playerId.startsWith("a_"))
            throw new InvalidFailException(AuthFailCode.INVALID_PLAYER_ID);

        /* 회원 등록 여부 확인 */
        Member member = memberRepository.findByPlayerId(playerId)
                .orElseGet(() -> {
                    // 없으면 회원으로 등록
                    String password = passwordEncoder.encode(UUID.randomUUID().toString());

                    Member registerMember = memberRepository.save(Member.builder()
                            .playerId(playerId)
                            .password(password)
                            .build());

                    Long memberId = registerMember.getMemberId();
                    payPointRepository.save(PayPoint.builder()
                            .point(0L)
                            .memberId(memberId)
                            .build());

                    return registerMember;
                });

        /* 기존 토큰이 존재하면 만료 시킴 */
        String memberId = String.valueOf(member.getMemberId());
        Session session = sessionRepository.findSessionTokenById(memberId)
                .orElseGet(() -> Session.builder()
                        .memberId(memberId)
                        .accessToken(new HashMap<>())
                        .refreshToken(new HashMap<>())
                        .build());

        // 엑세스 토큰이 존재하면 삭제
        if (session.getAccessToken().containsKey(deviceCode)) {
            String expireAccessToken = session.getAccessToken().get(deviceCode);
            sessionRepository.accessTokenDelete(expireAccessToken);
        }
        // 리프레시 토큰이 존재하면 삭제
        if (session.getRefreshToken().containsKey(deviceCode)) {
            String expireRefreshToken = session.getRefreshToken().get(deviceCode);
            sessionRepository.refreshTokenDelete(expireRefreshToken);
        }

        /* 새로운 토큰 발급 */
        // 기존 토큰이 존재해도 새로운 토큰으로 대체
        Token token = generateToken(memberId, deviceCode);
        session.getAccessToken().put(deviceCode, token.getAccessToken());
        session.getRefreshToken().put(deviceCode,token.getRefreshToken());
        sessionRepository.sessionTokenSave(memberId, session);

        return LoginResDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public ReissueResDto reissue(String refreshToken) throws RuntimeException  {
        // 리프레시 토큰 유무 확인
        if (!StringUtils.hasText(refreshToken))
            throw new InvalidFailException(AuthFailCode.NOT_FOUND_TOKEN);
        // 리프레시 토큰 형식 확인
        else if (!refreshToken.startsWith("Bearer "))
            throw new InvalidFailException(AuthFailCode.INVALID_TOKEN_FORM);
        else
            refreshToken = refreshToken.substring(7);

        // 만료 여부 확인
        if (tokenProvider.isTokenExpired(refreshToken))
            throw new InvalidFailException(AuthFailCode.EXPIRED_REFRESH_TOKEN);

        Refresh refresh = sessionRepository.findRefreshTokenById(refreshToken);
        // 강제 만료 여부 확인
        if (refresh == null)
            throw new InvalidFailException(AuthFailCode.EXPIRED_REFRESH_TOKEN);
        // 리프레시 토큰이 일치하는지 확인
        else if (!refreshToken.equals(refresh.getRefreshToken()))
            throw new InvalidFailException(AuthFailCode.UN_AUTHENTICATION_TOKEN);

        String memberId = refresh.getMemberId();
        DeviceCode deviceCode = refresh.getDeviceCode();

        String accessToken = tokenProvider.generateAccessToken(memberId);
        Session session = sessionRepository.findSessionTokenById(memberId)
                .orElseGet(() -> Session.builder()
                        .memberId(memberId)
                        .accessToken(new HashMap<>())
                        .refreshToken(new HashMap<>())
                        .build());

        // 기존 엑세스 토큰이 존재하면 삭제 (덮어쓰기전에)
        if (session.getAccessToken().containsKey(deviceCode)) {
            String expireAccessToken = session.getAccessToken().get(deviceCode);
            // 만료 시킬 토큰이 만료시간이 되지 않았으면 (레디스에 존재함) 삭제
            if (!tokenProvider.isTokenExpired(expireAccessToken)) {
                sessionRepository.accessTokenDelete(expireAccessToken);
            }
        }

        session.getAccessToken().put(deviceCode, accessToken);
        sessionRepository.sessionTokenSave(memberId, session);

        Access access = Access.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .deviceCode(deviceCode)
                .build();
        sessionRepository.accessTokenSave(accessToken, access, accessTokenExpired);

        return ReissueResDto.builder()
                .accessToken(accessToken)
                .build();
    }

    private Token generateToken(String memberId, DeviceCode deviceCode) {
        String accessToken = tokenProvider.generateAccessToken(memberId);
        String refreshToken = tokenProvider.generateRefreshToken(memberId);

        Access access = Access.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .deviceCode(deviceCode)
                .build();
        sessionRepository.accessTokenSave(accessToken, access, accessTokenExpired);

        Refresh refresh = Refresh.builder()
                .memberId(memberId)
                .refreshToken(refreshToken)
                .deviceCode(deviceCode)
                .build();
        sessionRepository.refreshTokenSave(refreshToken, refresh, refreshTokenExpired);

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
