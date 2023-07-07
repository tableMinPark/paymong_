package com.paymong.auth.global.config;

import com.paymong.auth.global.redis.SessionRepository;
import com.paymong.auth.global.security.CustomUserDetailService;
import com.paymong.auth.global.security.TokenAuthenticationFilter;
import com.paymong.auth.global.security.TokenExceptionFilter;
import com.paymong.auth.global.security.TokenProvider;
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
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;
    private final TokenExceptionFilter tokenExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors()
            .and()
            .csrf().disable()

            .authorizeRequests() // 요청에 대한 사용권한 체크
//            .antMatchers("/auth/**").hasAnyAuthority("ADMIN")
//            .antMatchers("/auth/login", "/auth/login/watch", "/auth/reissue").permitAll()
            .anyRequest().permitAll()

            .and()
            .exceptionHandling()

            .and()
            .logout().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable();

        http.addFilterBefore(new TokenAuthenticationFilter(tokenProvider, customUserDetailService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenExceptionFilter, TokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}