package com.paymong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FeignClientCode {
    SUCCESS("success"),
    FAIL("fail"),
    ERROR("error"),
    INTERNAL_SERVER("internal server");

    public final String status;
}
