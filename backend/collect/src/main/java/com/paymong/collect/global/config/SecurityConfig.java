package com.paymong.collect.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.collect.global.jwt.InternalTokenProvider;
import com.paymong.collect.global.security.AccessDeniedHandler;
import com.paymong.collect.global.security.AuthenticationEntryPoint;
import com.paymong.collect.global.security.TokenAuthenticationFilter;
import com.paymong.collect.global.security.TokenExceptionFilter;
import com.paymong.core.code.RoleCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final InternalTokenProvider internalTokenProvider;
    private final ObjectMapper objectMapper;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf().disable()

            .authorizeRequests()
            .antMatchers("/collect/**").hasAnyAuthority(RoleCode.ADMIN.getName())

            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(authenticationEntryPoint)

            .and()
            .logout().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable();

        http.addFilterBefore(new TokenAuthenticationFilter(internalTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new TokenExceptionFilter(objectMapper), TokenAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}