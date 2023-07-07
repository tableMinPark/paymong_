
package com.paymong.auth.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.secret}")
    private String JWT_KEY;

    @Value("${jwt.access_token_expired}")
    private Long ACCESS_TOKEN_EXPIRED;

    @Value("${jwt.refresh_token_expired}")
    private Long REFRESH_TOKEN_EXPIRED;

    public Claims extractAllClaims(String token) { // 2
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey(JWT_KEY))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String getMemberId(String accessToken) {
        return extractAllClaims(accessToken).get("memberId", String.class);
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateAccessToken(String memberId) {
        return doGenerateToken(memberId, ACCESS_TOKEN_EXPIRED);
    }

    public String generateRefreshToken(String memberId) {
        return doGenerateToken(memberId, REFRESH_TOKEN_EXPIRED);
    }

    private String doGenerateToken(String memberId, Long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expireTime))
            .signWith(getSigningKey(JWT_KEY), SignatureAlgorithm.HS256)
            .compact();
    }

    public Boolean validateToken(String accessToken, UserDetails userDetails) {
        String memberId = getMemberId(accessToken);
        return memberId.equals(userDetails.getUsername()) && !isTokenExpired(accessToken);
    }

    public long getRemainMilliSeconds(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }
}