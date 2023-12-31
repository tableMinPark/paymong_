package com.paymong.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.global.code.BasicFailCode;
import com.paymong.global.code.ErrorCode;
import com.paymong.global.exception.fail.InvalidFailException;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.exception.fail.UnAuthFailException;
import com.paymong.global.response.ErrorResponse;
import com.paymong.global.response.FailResponse;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenExceptionFilter extends GenericFilterBean {
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            chain.doFilter(request, response);
        } catch (NotFoundFailException | UnAuthFailException | InvalidFailException e) {
            String internalToken = request.getHeader("InternalToken");
            log.error("TokenExceptionFilter : 인증/인가에 필요한 정보가 없음 : {}", internalToken);
            setFailResponse((HttpServletResponse) response, e.getFailCode());
        } catch (Exception e) {
            e.printStackTrace();
            String internalToken = request.getHeader("InternalToken");
            log.error("TokenExceptionFilter : 알 수 없는 에러 발생, {}", internalToken);
            setErrorResponse((HttpServletResponse) response, ErrorCode.INTERNAL_SERVER);
        }
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

    public void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        response.setContentType("application/json; charset=UTF-8");

        try {
            response.setStatus(errorCode.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(errorCode)));
        } catch (IOException e){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
