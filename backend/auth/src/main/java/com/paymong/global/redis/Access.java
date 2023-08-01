package com.paymong.global.redis;

import javax.persistence.Id;

import com.paymong.global.code.DeviceCode;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Access {
    @Id
    private String accessToken;
    private String memberId;
    private DeviceCode deviceCode;
}
