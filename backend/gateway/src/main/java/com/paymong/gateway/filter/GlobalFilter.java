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
import java.util.Objects;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    @Data
    public static class Config {
        private boolean preLogger;
        private boolean postLogger;
    }

    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            LocalDateTime requestTime = LocalDateTime.now();

            ServerHttpRequest request = exchange.getRequest();

            if (config.preLogger) {
                String id = request.getId();
                String path = request.getPath().value();
                String method = request.getMethodValue();

                InetSocketAddress connection = request.getLocalAddress();
                String address = connection.getAddress().getHostAddress();
                String hostName = connection.getHostName();

                log.info("{} : GlobalFilter(in) : {} : {} : 아이피 - {} : 호스트네임 - {}", id, method, path, address, hostName);
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();

                if (config.postLogger) {
                    String id = request.getId();
                    int statusCode = Objects.requireNonNull(response.getStatusCode()).value();
                    long during = Duration.between(requestTime, LocalDateTime.now()).getNano();

                    log.info("{} : GlobalFilter(out) : 상태코드 - {} : 요청시간 - {} s", id, statusCode, during / 1000000000.0);
                }
            }));
        };
    }
}
