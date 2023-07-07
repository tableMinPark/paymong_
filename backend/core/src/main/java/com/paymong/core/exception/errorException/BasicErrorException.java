package com.paymong.core.exception.errorException;

import com.paymong.core.code.BasicFailCode;
import com.paymong.core.code.ErrorCode;

public class BasicErrorException extends RuntimeException {
    private ErrorCode errorCode;

    public BasicErrorException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
