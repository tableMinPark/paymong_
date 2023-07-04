package com.paymong.core.response;

public class SuccessResponse extends BasicResponse {
    Object data;

    public SuccessResponse(Object data) {
        super("success");
        this.data = data;
    }
}
