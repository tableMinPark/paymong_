package com.paymong.auth.global.code;

public enum RoleCode {
    US000("USER", "일반회원"),
    US001("ADMIN", "관리자");

    private final String code;
    private final String name;

    RoleCode(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
