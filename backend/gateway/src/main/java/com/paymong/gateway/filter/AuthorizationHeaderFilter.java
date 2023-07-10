package com.paymong.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.gateway.code.GatewayFailCode;
import com.paymong.gateway.exception.HeaderException;
import com.paymong.gateway.exception.InvalidException;
import com.paymong.gateway.jwt.ExternalTokenProvider;
import com.paymong.gateway.redis.Access;
import com.paymong.gateway.redis.Session;
import com.paymong.gateway.redis.SessionRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;

// 인가 필터
@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    private final ObjectMapper objectMapper;
    private final ExternalTokenProvider externalTokenProvider;
    private final SessionRepository sessionRepository;

    @Data
    public static class Config {
        private boolean preLogger;
    }

    public AuthorizationHeaderFilter(ObjectMapper objectMapper, ExternalTokenProvider externalTokenProvider, SessionRepository sessionRepository) {
        super(AuthorizationHeaderFilter.Config.class);
        this.objectMapper = objectMapper;
        this.externalTokenProvider = externalTokenProvider;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0);

            // 엑세스 토큰 유무 확인 (공백)
            if (!StringUtils.hasText(accessToken))
                throw new InvalidException(GatewayFailCode.NOT_FOUND_TOKEN);
            // 엑세스 토큰 형식 확인
            else if (!accessToken.startsWith("Bearer "))
                throw new InvalidException(GatewayFailCode.INVALID_TOKEN);
            else
                accessToken = accessToken.substring(7);

            // 엑세스 토큰 유효성 확인
            Access access = sessionRepository.findAccessTokenById(accessToken);
            if (access == null || externalTokenProvider.isTokenExpired(accessToken))
                throw new InvalidException(GatewayFailCode.EXPIRED_ACCESS_TOKEN);
            else if (!accessToken.equals(access.getAccessToken()))
                throw new InvalidException(GatewayFailCode.UN_AUTHENTICATION_TOKEN);

            try {
                // 토큰에서 memberId 추출 후 헤더에 삽입
                String memberId = externalTokenProvider.getMemberId(accessToken);
                request.mutate().header("MemberId", memberId).build();

                // 비밀번호 생성해서 헤더에 삽입
                String password = UUID.randomUUID().toString();
                request.mutate().header("Password", password).build();

                // 레디스에서 역할 조회 후 헤더에 삽입
                Session session = sessionRepository.findSessionTokenById(memberId)
                        .orElseThrow(() -> new InvalidException(GatewayFailCode.EXPIRED_ACCESS_TOKEN));
                String roles = objectMapper.writeValueAsString(session.getRoles());
                request.mutate().header("Roles", roles).build();


                // 인증 정보를 담았다는 정보를 저장
                request.mutate().header("IsAuth", "true").build();

                if (config.preLogger) {
                    String id = request.getId();
                    String path = request.getPath().value();
                    log.info("AuthorizationHeaderFilter : 회원 정보 추출 : {} : {} : {} : {} : {}", id, path, accessToken, memberId, roles);
                }

                return chain.filter(exchange);
            } catch (JsonProcessingException e) {
                throw new HeaderException(GatewayFailCode.NOT_FOUND_ROLES);
            }
        };
    }
}