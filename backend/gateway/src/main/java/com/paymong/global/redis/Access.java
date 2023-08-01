package com.paymong.global.redis;

import com.paymong.global.code.DeviceCode;
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
