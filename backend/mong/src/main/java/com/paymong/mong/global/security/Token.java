package com.paymong.mong.global.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Token {
    private String accessToken;
    private String refreshToken;
}
