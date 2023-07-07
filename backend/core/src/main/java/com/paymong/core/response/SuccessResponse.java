package com.paymong.core.response;

import lombok.Getter;

@Getter
public class SuccessResponse extends BasicResponse {
    Object data;

    public SuccessResponse(Object data) {
        super("success");
        this.data = data;
    }
}
