package com.paymong.core.code;

public enum ErrorCode {
    INTERNAL_SERVER("서버 내부 에러", 500);

    private final String message;
    private final Integer code;

    ErrorCode(final String message, final Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getCode() {
        return this.code;
    }

}
