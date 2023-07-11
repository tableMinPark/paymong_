package com.paymong.auth.global.redis;

import com.paymong.auth.entity.Role;
import com.paymong.core.code.DeviceCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
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
