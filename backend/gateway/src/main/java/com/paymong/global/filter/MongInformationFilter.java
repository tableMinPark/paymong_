package com.paymong.global.filter;

import com.paymong.gateway.entity.Mong;
import com.paymong.gateway.repository.MongRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MongInformationFilter extends AbstractGatewayFilterFactory<MongInformationFilter.Config> {
    private final MongRepository mongRepository;

    @Data
    public static class Config {
        private boolean preLogger;
    }

    public MongInformationFilter(MongRepository mongRepository) {
        super(Config.class);
        this.mongRepository = mongRepository;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String memberId = exchange.getAttribute("memberId");
            Mong mong = mongRepository.findByMemberId(Long.parseLong(memberId))
                    .orElseGet(() -> Mong.builder().mongId(-1L).build());
            String mongId = String.valueOf(mong.getMongId());

            exchange.getAttributes().put("mongId", mongId);

            if (config.preLogger) {
                String id = request.getId();
                String path = request.getPath().value();
                String method = request.getMethodValue();
                log.info("{} : MongInformationFilter : {} : {} : 회원 - {} : 몽 - {} : 몽 관련 정보 추출", id, method, path, memberId, mongId);
            }

            return chain.filter(exchange);
        };
    }
}
