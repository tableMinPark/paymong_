package com.paymong.global.config;

import com.paymong.global.security.CustomUserDetail;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

            String internalToken = customUserDetail.getInternalToken();
            internalToken = String.format("Bearer %s", internalToken);

            requestTemplate.header("InternalToken", internalToken);
        };
    }
}