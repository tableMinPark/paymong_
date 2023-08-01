package com.paymong.global.redis;

import com.paymong.global.code.DeviceCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
public class Refresh {
    @Id
    private String refreshToken;
    private String memberId;
    private DeviceCode deviceCode;
}
