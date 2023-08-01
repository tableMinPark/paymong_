package com.paymong.auth.service;

import com.paymong.auth.dto.request.LoginReqDto;
import com.paymong.auth.dto.response.LoginResDto;
import com.paymong.auth.dto.response.ReissueResDto;
import com.paymong.auth.entity.CommonCode;
import com.paymong.auth.entity.Member;
import com.paymong.auth.entity.PayPoint;
import com.paymong.auth.entity.Role;
import com.paymong.auth.repository.MemberRepository;
import com.paymong.auth.repository.PayPointRepository;
import com.paymong.auth.repository.RoleRepository;
import com.paymong.global.code.AuthFailCode;
import com.paymong.global.redis.Access;
import com.paymong.global.redis.Refresh;
import com.paymong.global.redis.Session;
import com.paymong.global.redis.SessionRepository;
import com.paymong.global.security.Token;
import com.paymong.global.jwt.ExternalTokenProvider;
import com.paymong.global.code.DeviceCode;
import com.paymong.global.code.RoleCode;
import com.paymong.global.exception.fail.InvalidFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PayPointRepository payPointRepository;
    private final RoleRepository roleRepository;
    private final SessionRepository sessionRepository;
    private final ExternalTokenProvider externalTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.external.access_token_expired}")
    private Long accessTokenExpired;
    @Value("${jwt.external.refresh_token_expired}")
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
                    // 회원 정보 저장
                    Member registerMember = memberRepository.save(Member.builder()
                            .playerId(playerId)
                            .build());

                    // 포인트 정보 저장
                    Long memberId = registerMember.getMemberId();
                    payPointRepository.save(PayPoint.builder()
                            .memberId(memberId)
                            .point(0)
                            .build());

                    // 역할 저장
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
                        .roles(new ArrayList<>() {{ add(RoleCode.USER.getName()); }})
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

        // 역할 갱신
        List<Role> roles = roleRepository.findByMemberId(member.getMemberId());
        session.setRoles(roles.stream()
                .map((role -> role.getCode().getName()))
                .collect(Collectors.toList()));
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
        Refresh refresh = sessionRepository.findRefreshTokenById(refreshToken);
        if (refresh == null || externalTokenProvider.isTokenExpired(refreshToken))
            throw new InvalidFailException(AuthFailCode.EXPIRED_REFRESH_TOKEN);

        // 리프레시 토큰이 일치하는지 확인
        else if (!refreshToken.equals(refresh.getRefreshToken()))
            throw new InvalidFailException(AuthFailCode.UN_AUTHENTICATION_TOKEN);

        String memberId = refresh.getMemberId();
        DeviceCode deviceCode = refresh.getDeviceCode();

        String accessToken = externalTokenProvider.generateAccessToken(memberId);
        Session session = sessionRepository.findSessionTokenById(memberId)
                .orElseGet(() -> Session.builder()
                        .memberId(memberId)
                        .accessToken(new HashMap<>())
                        .refreshToken(new HashMap<>())
                        .roles(new ArrayList<>() {{ add(RoleCode.USER.getName()); }})
                        .build());

        // 기존 엑세스 토큰이 존재하면 삭제 (덮어쓰기전에)
        if (session.getAccessToken().containsKey(deviceCode)) {
            String expireAccessToken = session.getAccessToken().get(deviceCode);
            // 만료 시킬 토큰이 만료시간이 되지 않았으면 (레디스에 존재함) 삭제
            if (!externalTokenProvider.isTokenExpired(expireAccessToken)) {
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
