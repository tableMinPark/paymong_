package com.paymong.gateway.redis;

import com.paymong.gateway.code.DeviceCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
public class Access {
    @Id
    private String accessToken;
    private String memberId;
    private DeviceCode deviceCode;
}
