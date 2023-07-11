package com.paymong.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MongInformationFilter extends AbstractGatewayFilterFactory<MongInformationFilter.Config> {

    @Data
    public static class Config {
        private boolean preLogger;
    }

    public MongInformationFilter() { super(Config.class); }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String memberId = exchange.getAttribute("memberId");

            // memberId를 기준으로 redis 에서 mongId 조회 후 mongId 헤더에 삽입
            String mongId = "1";

            exchange.getAttributes().put("mongId", mongId);

            if (config.preLogger) {
                String id = request.getId();
                String path = request.getPath().value();
                log.info("MongInformationHeaderFilter : 몽 정보 추출 : {} : {} : {} : {}", id, path, memberId, mongId);
            }

            return chain.filter(exchange);
        };
    }
}
