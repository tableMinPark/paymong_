package com.paymong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeignClientCode {
    FAIL("fail"),
    ERROR("error"),
    INTERNAL_SERVER("internal server");

    private final String status;
}
