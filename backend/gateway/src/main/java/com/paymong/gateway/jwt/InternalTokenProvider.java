package com.paymong.gateway.jwt;

import com.paymong.gateway.code.GatewayFailCode;
import com.paymong.gateway.exception.InvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InternalTokenProvider {

    @Value("${jwt.internal.secret}")
    private String JWT_KEY;

    @Value("${jwt.internal.access_token_expired}")
    private Long ACCESS_TOKEN_EXPIRED;

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String memberId, String roles, String mongId) {
        return doGenerateToken(memberId, roles, mongId, ACCESS_TOKEN_EXPIRED);
    }

    private String doGenerateToken(String memberId, String roles, String mongId, Long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        claims.put("roles", roles);
        claims.put("mongId", mongId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(JWT_KEY), SignatureAlgorithm.HS256)
                .compact();
    }
}