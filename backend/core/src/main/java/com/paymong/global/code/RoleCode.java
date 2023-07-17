package com.paymong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RoleCode {
    USER("US000", "USER"),
    ADMIN("US001", "ADMIN");

    public final String code;
    public final String name;
}
