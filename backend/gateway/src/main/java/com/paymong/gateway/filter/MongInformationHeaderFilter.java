package com.paymong.gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MongInformationHeaderFilter extends AbstractGatewayFilterFactory<MongInformationHeaderFilter.Config> {

    @Data
    public static class Config {
        private boolean preLogger;
    }

    public MongInformationHeaderFilter() { super(Config.class); }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

                String id = request.getId();
                String path = request.getPath().value();
                String memberId = request.getHeaders().get("MemberId").get(0);

                // memberId를 기준으로 redis에서 mongId 조회

                String mongId = "1";

                request.mutate().header("mongId", mongId).build();

            if (config.preLogger) {
                log.info("MongInformationHeaderFilter : {} : {} : {} : {}", id, path, memberId, mongId);
            }

            return chain.filter(exchange);
        };
    }
}
