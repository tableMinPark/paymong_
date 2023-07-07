package com.paymong.auth.global.redis;

import javax.persistence.Id;

import com.paymong.core.code.DeviceCode;
import lombok.*;
import org.springframework.security.core.token.Token;

@Data
@Builder
@AllArgsConstructor
public class Access {
    @Id
    private String accessToken;
    private String memberId;
    private DeviceCode deviceCode;
}
