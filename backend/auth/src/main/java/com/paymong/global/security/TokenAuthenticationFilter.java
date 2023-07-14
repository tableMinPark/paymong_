package com.paymong.global.security;

import com.paymong.global.jwt.InternalTokenProvider;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final InternalTokenProvider internalTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws RuntimeException, ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String internalToken = request.getHeader("InternalToken");

        if (internalToken != null) {
            internalToken = internalToken.substring(7);

            String memberId = internalTokenProvider.getMemberId(internalToken);
            List<String> roles = internalTokenProvider.getRoles(internalToken);

            UserDetails userDetails = CustomUserDetail.of(memberId, internalToken, roles);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            log.info("TokenAuthenticationFilter : Auth 서비스 AccessToken 으로 접근 : {} : {} : {}", memberId, userDetails.getAuthorities(), internalToken);
        }

        chain.doFilter(request, response);
    }
}
