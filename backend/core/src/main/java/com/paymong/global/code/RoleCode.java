package com.paymong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleCode {
    USER("US000", "USER"),
    ADMIN("US001", "ADMIN");

    private final String code;
    private final String name;
}
