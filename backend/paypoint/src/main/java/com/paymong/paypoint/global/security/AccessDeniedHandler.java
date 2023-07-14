package com.paymong.paypoint.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.core.code.BasicFailCode;
import com.paymong.core.code.FailCode;
import com.paymong.core.response.FailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    // 스프링 시큐리티 인가 실패 핸들러
    // 접근 권한이 없어 인가가 불가한 경우에 해당 핸들러가 응답
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        String internalToken = request.getHeader("InternalToken");
        log.info("AuthenticationEntryPoint : 인가 불가 : {}", internalToken);
        setFailResponse(response, FailCode.FORBIDDEN);
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
