package com.paymong.gateway.exception;

import com.paymong.gateway.code.GatewayErrorCode;
import com.paymong.gateway.code.GatewayFailCode;
import com.paymong.gateway.response.ErrorResponse;
import com.paymong.gateway.response.FailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.Order;
import org.springframework.core.codec.Hints;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

@Slf4j
@Component
@Order(-1)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    // gateway 전역 exception 핸들러
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        ServerHttpRequest request = exchange.getRequest();

        String method = request.getMethodValue();
        String id = request.getId();
        String path = request.getPath().value();

        if (e instanceof NullPointerException) {
            log.info("AuthorizationHeaderFilter : 헤더에 엑세스 토큰 없음 : {} : {} : {}", id, method, path);
            return setFailResponse(exchange, GatewayFailCode.NOT_FOUND_TOKEN);
        } else if (e instanceof InvalidException) {
            log.info("AuthorizationHeaderFilter : 유효하지 않은 토큰 : {} : {} : {}", id, method, path);
            return setFailResponse(exchange, ((InvalidException) e).getFailCode());
        } else if (e instanceof HeaderException) {
            log.info("AuthorizationHeaderFilter : 헤더에 토큰 저장 불가 : {} : {} : {}", id, method, path);
            return setFailResponse(exchange, ((HeaderException) e).getFailCode());
        } else if (e instanceof NotFoundException || e instanceof ConnectException) {
            log.info("AuthorizationHeaderFilter : 마이크로 서비스 접근 불가 : {} : {} : {}", id, method, path);
            return setErrorResponse(exchange, GatewayErrorCode.INVALID_SERVER);
        } else {
            e.printStackTrace();
            log.info("AuthorizationHeaderFilter : 인증 불가 : {} : {} : {}", id, method, path);
            return setFailResponse(exchange, GatewayFailCode.UN_AUTHENTICATION);
        }
    }

    private Mono<Void> setFailResponse(ServerWebExchange exchange, GatewayFailCode failCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(failCode.getHttpStatus());
        FailResponse failResponse = new FailResponse(failCode);

        return response.writeWith(
                new Jackson2JsonEncoder()
                        .encode(Mono.just(failResponse),
                                response.bufferFactory(),
                                ResolvableType.forInstance(failResponse),
                                MediaType.APPLICATION_JSON,
                                Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix()))
        );
    }
    private Mono<Void> setErrorResponse(ServerWebExchange exchange, GatewayErrorCode errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(errorCode.getHttpStatus());
        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        return response.writeWith(
                new Jackson2JsonEncoder()
                        .encode(Mono.just(errorResponse),
                                response.bufferFactory(),
                                ResolvableType.forInstance(errorResponse),
                                MediaType.APPLICATION_JSON,
                                Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix()))
        );
    }
}