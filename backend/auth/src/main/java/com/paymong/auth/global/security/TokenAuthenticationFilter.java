package com.paymong.auth.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.auth.global.redis.Access;
import com.paymong.auth.global.redis.SessionRepository;
import com.paymong.core.code.FailCode;
import com.paymong.core.exception.fail.InvalidFailException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends GenericFilterBean {
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws RuntimeException, ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        boolean isAuth = Boolean.parseBoolean(request.getHeader("IsAuth"));

        if (isAuth) {
            String memberId = request.getHeader("MemberId");
            String password = request.getHeader("Password");
            List<String> roles = Arrays.asList(
                    objectMapper.readValue(request.getHeader("Roles"), String[].class));

            UserDetails userDetails = CustomUserDetail.of(memberId, password, roles);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            log.info("TokenAuthenticationFilter : Auth 서비스 AccessToken 으로 접근 : {} : {}", memberId, userDetails.getAuthorities());
        }

        chain.doFilter(request, response);
    }
}
