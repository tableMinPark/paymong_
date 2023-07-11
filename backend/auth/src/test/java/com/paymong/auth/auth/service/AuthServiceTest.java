package com.paymong.auth.auth.service;

import com.paymong.auth.entity.CommonCode;
import com.paymong.auth.entity.Member;
import com.paymong.auth.entity.PayPoint;
import com.paymong.auth.entity.Role;
import com.paymong.auth.repository.MemberRepository;
import com.paymong.auth.repository.PayPointRepository;
import com.paymong.auth.repository.RoleRepository;
import com.paymong.auth.global.code.AuthFailCode;
import com.paymong.auth.global.redis.Access;
import com.paymong.auth.global.redis.Refresh;
import com.paymong.auth.global.redis.Session;
import com.paymong.auth.global.redis.SessionRepository;
import com.paymong.auth.global.security.Token;
import com.paymong.auth.global.jwt.ExternalTokenProvider;
import com.paymong.core.code.DeviceCode;
import com.paymong.core.code.RoleCode;
import com.paymong.core.exception.fail.InvalidFailException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PayPointRepository payPointRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ExternalTokenProvider externalTokenProvider;

    @Value("${jwt.access_token_expired}")
    private Long accessTokenExpired;
    @Value("${jwt.refresh_token_expired}")
    private Long refreshTokenExpired;

    @Test
    @Transactional
    void registerMemberTest() {
        String playerId = "a_test";

        Member registerMember = memberRepository.save(Member.builder()
                .playerId(playerId)
                .build());

        Long memberId = registerMember.getMemberId();
        payPointRepository.save(PayPoint.builder()
                .memberId(memberId)
                .point(0L)
                .build());

        roleRepository.save(Role.builder()
                .memberId(memberId)
                .code(CommonCode.builder()
                        .code(RoleCode.USER.getCode())
                        .build())
                .build());

        assertNotNull(registerMember.getMemberId());
    }

    @Test
    @Transactional
    void loginTest() {
        /* 입력 */
        String playerId = "a_test";
        DeviceCode deviceCode = DeviceCode.APP;

        /* 로그인 로직 시작 */
        /* 플레이어 아이디 형식 확인 */
        if (!StringUtils.hasText(playerId) || !playerId.startsWith("a_"))
            throw new InvalidFailException(AuthFailCode.INVALID_PLAYER_ID);

        /* 회원 등록 여부 확인 */
        Member member = memberRepository.findByPlayerId(playerId)
                .orElseGet(() -> {
                    // 없으면 등록 (회원 등록)
                    Member registerMember = memberRepository.save(Member.builder()
                            .playerId(playerId)
                            .build());

                    Long memberId = registerMember.getMemberId();
                    payPointRepository.save(PayPoint.builder()
                            .point(0L)
                            .memberId(memberId)
                            .build());

                    roleRepository.save(Role.builder()
                            .memberId(memberId)
                            .code(CommonCode.builder()
                                    .code(RoleCode.USER.getCode())
                                    .build())
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

        if (session.getAccessToken().containsKey(deviceCode)) {
            String expireAccessToken = session.getAccessToken().get(deviceCode);
            sessionRepository.accessTokenDelete(expireAccessToken);
        }
        if (session.getRefreshToken().containsKey(deviceCode)) {
            String expireRefreshToken = session.getRefreshToken().get(deviceCode);
            sessionRepository.refreshTokenDelete(expireRefreshToken);
        }

        /* 검증 준비 */
        // 만료 검증을 위한 이전 토큰 저장
        String pastAccessToken = session.getAccessToken().getOrDefault(deviceCode, "");
        String pastRefreshToken = session.getRefreshToken().getOrDefault(deviceCode, "");

        /* 새로운 토큰 발급 */
        Token token = generateToken(memberId, deviceCode);
        session.getAccessToken().put(deviceCode, token.getAccessToken());
        session.getRefreshToken().put(deviceCode,token.getRefreshToken());
        sessionRepository.sessionTokenSave(memberId, session);
        /* 로그인 로직 끝 */

        /* 검증 */
        // 기존 토큰 삭제 검증
        assertNull(sessionRepository.findAccessTokenById(pastAccessToken));
        assertNull(sessionRepository.findRefreshTokenById(pastRefreshToken));
        session = sessionRepository.findSessionTokenById(memberId)
                .orElseGet(() -> Session.builder()
                        .memberId(memberId)
                        .accessToken(new HashMap<>())
                        .refreshToken(new HashMap<>())
                        .build());
        assertEquals(token.getAccessToken(), session.getAccessToken().get(deviceCode));
        assertEquals(token.getRefreshToken(), session.getRefreshToken().get(deviceCode));

        // 검증 후 데이터 삭제
        sessionRepository.sessionTokenDelete(memberId);
        sessionRepository.accessTokenDelete(token.getAccessToken());
        sessionRepository.refreshTokenDelete(token.getRefreshToken());

        assertNull(sessionRepository.findAccessTokenById(token.getAccessToken()));
        assertNull(sessionRepository.findRefreshTokenById(token.getRefreshToken()));
        assertFalse(sessionRepository.findSessionTokenById(memberId).isPresent());
    }

    @Test
    @Transactional
    void reissueTest() {
        /* 입력 */
        String refreshToken = beforeReissueTest();

        /* 재발급 로직 시작 */
        // 리프레시 토큰 형식 확인
        if (!StringUtils.hasText(refreshToken) || !refreshToken.startsWith("Bearer "))
            throw new InvalidFailException(AuthFailCode.INVALID_TOKEN_FORM);
        else
            refreshToken = refreshToken.substring(7);
        // 강제 만료 여부 확인
        if (sessionRepository.findRefreshTokenById(refreshToken) == null)
            throw new InvalidFailException(AuthFailCode.EXPIRED_REFRESH_TOKEN);
        // 만료 여부 확인
        if (externalTokenProvider.isTokenExpired(refreshToken))
            throw new InvalidFailException(AuthFailCode.EXPIRED_REFRESH_TOKEN);

        Refresh refresh = sessionRepository.findRefreshTokenById(refreshToken);
        String memberId = refresh.getMemberId();
        DeviceCode deviceCode = refresh.getDeviceCode();

        String accessToken = externalTokenProvider.generateAccessToken(memberId);
        Session session = sessionRepository.findSessionTokenById(memberId)
                .orElseGet(() -> Session.builder()
                        .memberId(memberId)
                        .accessToken(new HashMap<>())
                        .refreshToken(new HashMap<>())
                        .build());
        session.getAccessToken().put(deviceCode, accessToken);
        sessionRepository.sessionTokenSave(memberId, session);

        Access access = Access.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .deviceCode(deviceCode)
                .build();
        sessionRepository.accessTokenSave(accessToken, access, accessTokenExpired);
        /* 재발급 로직 끝 */

        /* 검증 */
        // 엑세스 토큰 재발급 검증 및 Session 데이터 검증
        access = sessionRepository.findAccessTokenById(accessToken);
        session = sessionRepository.findSessionTokenById(memberId)
                .orElseGet(() -> Session.builder()
                        .memberId(memberId)
                        .accessToken(new HashMap<>())
                        .refreshToken(new HashMap<>())
                        .build());
        assertNotNull(access.getAccessToken());
        assertTrue(session.getAccessToken().containsValue(accessToken));

        // 검증 후 데이터 삭제
        sessionRepository.sessionTokenDelete(memberId);
        sessionRepository.accessTokenDelete(accessToken);
        sessionRepository.refreshTokenDelete(refreshToken);
    }


    /* BEFORE EACH TEST METHOD */
    String beforeReissueTest() {
        String playerId = "a_test";
        DeviceCode deviceCode = DeviceCode.APP;

        Member member = memberRepository.findByPlayerId(playerId)
                .orElseGet(() -> {
                    // 없으면 등록 (회원 등록)
                    Long memberId = 0L;
                    Member registerMember = memberRepository.save(Member.builder()
                            .memberId(memberId)
                            .playerId(playerId)
                            .build());

                    payPointRepository.save(PayPoint.builder()
                            .point(0L)
                            .memberId(memberId)
                            .build());

                    roleRepository.save(Role.builder()
                            .memberId(memberId)
                            .code(CommonCode.builder()
                                    .code(RoleCode.USER.getCode())
                                    .build())
                            .build());

                    return registerMember;
                });

        String memberId = String.valueOf(member.getMemberId());
        String refreshToken = externalTokenProvider.generateRefreshToken(memberId);

        Session session = Session.builder()
                .memberId(memberId)
                .accessToken(new HashMap<>())
                .refreshToken(new HashMap<>())
                .build();
        session.getRefreshToken().put(deviceCode, refreshToken);

        sessionRepository.sessionTokenSave(memberId, session);
        sessionRepository.refreshTokenSave(refreshToken, Refresh.builder()
                .memberId(memberId)
                .deviceCode(deviceCode)
                .refreshToken(refreshToken)
                .build(), refreshTokenExpired);

        return String.format("Bearer %s", refreshToken);
    }

    /* TOKEN */
    private Token generateToken(String memberId, DeviceCode deviceCode) {
        String accessToken = externalTokenProvider.generateAccessToken(memberId);
        String refreshToken = externalTokenProvider.generateRefreshToken(memberId);

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