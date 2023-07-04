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

            if (config.preLogger) {
                ServerHttpRequest request = exchange.getRequest();
                String id = request.getId();
                String path = request.getPath().value();

                InetSocketAddress connection = request.getLocalAddress();
                String address = connection.getAddress().getHostAddress();
                String hostName = connection.getHostName();

                log.info("{} : GlobalFilter(request) : {} : {} : {}", LocalDateTime.now(), address, hostName, id);
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    ServerHttpResponse response = exchange.getResponse();
                    int statusCode = response.getStatusCode().value();
                    long during = Duration.between(requestTime, LocalDateTime.now()).getSeconds();

                    log.info("{} : GlobalFilter(response) : {} : {}s", LocalDateTime.now(), statusCode, during);
                }
            }));
        };
    }
}
