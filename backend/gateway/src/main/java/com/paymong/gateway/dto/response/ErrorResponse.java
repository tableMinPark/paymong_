package com.paymong.gateway.dto.response;

import com.paymong.global.code.GatewayErrorCode;
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
