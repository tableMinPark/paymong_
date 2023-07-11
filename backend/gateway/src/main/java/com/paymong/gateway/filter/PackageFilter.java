package com.paymong.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.gateway.code.GatewayFailCode;
import com.paymong.gateway.exception.HeaderException;
import com.paymong.gateway.jwt.InternalTokenProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Slf4j
public class PackageFilter extends AbstractGatewayFilterFactory<com.paymong.gateway.filter.PackageFilter.Config> {
    // AuthorizationHeaderFileter 와 MongInfomationHeaderFilter 에서
    // 삽입한 정보들을 JWT 토큰 payload에 패키징해서 토큰화하는 필터
    private final ObjectMapper objectMapper;
    private final InternalTokenProvider internalTokenProvider;

    @Data
    public static class Config {
        private boolean preLogger;
    }

    public PackageFilter(ObjectMapper objectMapper, InternalTokenProvider internalTokenProvider) {
        super(PackageFilter.Config.class);
        this.objectMapper = objectMapper;
        this.internalTokenProvider = internalTokenProvider;
    }

    @Override
    public GatewayFilter apply(PackageFilter.Config config) {
        return (exchange, chain) -> {

            try {
                ServerHttpRequest request = exchange.getRequest();
                String memberId = exchange.getAttribute("memberId");
                String roles = objectMapper.writeValueAsString(exchange.getAttribute("roles"));
                String mongId = exchange.getAttribute("mongId");

                // JWT 토큰으로 패키징
                String internalToken = internalTokenProvider.generateAccessToken(memberId, roles, mongId);
                internalToken = String.format("Bearer %s", internalToken);

                request.mutate().header("InternalToken", internalToken).build();

                if (config.preLogger) {
                    log.info("PackageFilter : {} : {} : {} : {}", memberId, roles, mongId, internalToken);
                }
            } catch (JsonProcessingException e) {
                throw new HeaderException(GatewayFailCode.NOT_REIGSTER_TOKEN);
            }

            return chain.filter(exchange);
        };
    }
}
