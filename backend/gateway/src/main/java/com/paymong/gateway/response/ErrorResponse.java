package com.paymong.gateway.response;

import com.paymong.gateway.code.GatewayErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse extends BasicResponse {
    private final String message;
    private final Integer code;

    public ErrorResponse(GatewayErrorCode errorCode){
        super("error");
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }
}
