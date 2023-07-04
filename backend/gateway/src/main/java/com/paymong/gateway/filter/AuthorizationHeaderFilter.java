package com.paymong.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

// 인가 필터
@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Data
    public static class Config {
        private boolean preLogger;
    }

    public AuthorizationHeaderFilter() {
        super(AuthorizationHeaderFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String id = request.getId();
            String path = request.getPath().value();

            // 토큰까서 memberId 추출
            String memberId = "0";

            request.mutate().header("MemberId", memberId).build();

            if (config.preLogger) {
                log.info("{} : AuthorizationHeaderFilter(request) : {} : {} : {}", LocalDateTime.now(), id, path, memberId);
            }

            return chain.filter(exchange);
        };
    }
}
