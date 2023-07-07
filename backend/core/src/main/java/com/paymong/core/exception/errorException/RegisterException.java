package com.paymong.core.exception.errorException;

import com.paymong.core.code.ErrorCode;

public class RegisterException extends BasicErrorException {
    public RegisterException(ErrorCode errorCode) {
        super(errorCode);
    }
}
