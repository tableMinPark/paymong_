package com.paymong.auth.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.core.code.BasicFailCode;
import com.paymong.core.code.ErrorCode;
import com.paymong.core.exception.failException.InvalidFailException;
import com.paymong.core.exception.failException.NotFoundFailException;
import com.paymong.core.exception.failException.UnAuthFailException;
import com.paymong.core.response.ErrorResponse;
import com.paymong.core.response.FailResponse;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            chain.doFilter(request, response);
        } catch (NotFoundFailException e) {
            setFailResponse(HttpStatus.BAD_REQUEST, (HttpServletResponse) response, e.getFailCode());
        } catch (UnAuthFailException e) {
            setFailResponse(HttpStatus.UNAUTHORIZED, (HttpServletResponse) response, e.getFailCode());
        } catch (InvalidFailException e) {
            setFailResponse(HttpStatus.FORBIDDEN, (HttpServletResponse) response, e.getFailCode());
        } catch (Exception e) {
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, (HttpServletResponse) response, ErrorCode.INTERNAL_SERVER);
        }
    }

    public void setFailResponse(HttpStatus status, HttpServletResponse response, BasicFailCode failCode) {
        response.setContentType("application/json; charset=UTF-8");

        try {
            response.setStatus(status.value());
            response.getWriter().write(objectMapper.writeValueAsString(new FailResponse(failCode)));
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, ErrorCode errorCode) {
        response.setContentType("application/json; charset=UTF-8");

        try {
            response.setStatus(status.value());
            response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(errorCode)));
        } catch (IOException e){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
