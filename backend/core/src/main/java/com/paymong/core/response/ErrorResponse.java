package com.paymong.core.response;

import com.paymong.core.code.ErrorCode;

public class ErrorResponse extends BasicResponse{
    String message;
    Integer code;

    public ErrorResponse(ErrorCode errorCode){
        super("error");
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }
}
