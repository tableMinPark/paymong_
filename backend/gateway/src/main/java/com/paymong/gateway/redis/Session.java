package com.paymong.gateway.redis;

import com.paymong.gateway.code.DeviceCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Session {
    @Id
    private String memberId;
    private Map<DeviceCode, String> accessToken;
    private Map<DeviceCode, String> refreshToken;
    private List<String> roles;
}
