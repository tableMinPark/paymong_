package com.paymong.global.filter;

import com.paymong.global.code.GatewayFailCode;
import com.paymong.global.exception.InvalidException;
import com.paymong.global.jwt.ExternalTokenProvider;
import com.paymong.global.redis.Access;
import com.paymong.global.redis.Session;
import com.paymong.global.redis.SessionRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

// 인가 필터
@Slf4j
@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {
    private final ExternalTokenProvider externalTokenProvider;
    private final SessionRepository sessionRepository;

    @Data
    public static class Config {
        private boolean preLogger;
    }

    public AuthorizationFilter(ExternalTokenProvider externalTokenProvider, SessionRepository sessionRepository) {
        super(AuthorizationFilter.Config.class);
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

            // 토큰에서 memberId 추출 후 Attribute 에 저장 (PackageFilter 에서 토큰화 후 Header 에 저장)
            String memberId = externalTokenProvider.getMemberId(accessToken);
            exchange.getAttributes().put("memberId", memberId);

            // 레디스에서 역할 조회 후 Attribute 에 저장 (PackageFilter 에서 토큰화 후 Header 에 저장)
            Session session = sessionRepository.findSessionTokenById(memberId)
                    .orElseThrow(() -> new InvalidException(GatewayFailCode.EXPIRED_ACCESS_TOKEN));
            List<String> roles = session.getRoles();
            exchange.getAttributes().put("roles", roles);

            if (config.preLogger) {
                String id = request.getId();
                String path = request.getPath().value();
                String method = request.getMethodValue();
                log.info("{} : AuthorizationFilter : 회원 - {} : 권한 - {} : 회원 정보 추출", id, memberId, roles);
            }

            return chain.filter(exchange);
        };
    }
}