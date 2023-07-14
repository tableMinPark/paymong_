package com.paymong.paypoint.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.core.code.BasicFailCode;
import com.paymong.core.code.FailCode;
import com.paymong.core.response.FailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    // 스프링 시큐리티 인증 불가 핸들러
    // 유효한 토큰이 없어 인증에 필요한 정보가 없는 경우 해당 핸들러가 응답
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        String internalToken = request.getHeader("InternalToken");
        log.info("AuthenticationEntryPoint : 인증 불가 : {}", internalToken);
        setFailResponse(response, FailCode.UN_AUTHENTICATION);
    }

    public void setFailResponse(HttpServletResponse response, BasicFailCode failCode) {
        response.setContentType("application/json; charset=UTF-8");

        try {
            response.setStatus(failCode.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(new FailResponse(failCode)));
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}