package com.paymong.core.response;

import com.paymong.core.code.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse extends BasicResponse{
    String message;
    Integer code;

    public ErrorResponse(ErrorCode errorCode){
        super("error");
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }
}
