package com.paymong.global.response;

import lombok.Getter;

@Getter
public class SuccessResponse extends BasicResponse {
    private final Object data;

    public SuccessResponse(Object data) {
        super("success");
        this.data = data;
    }
}
