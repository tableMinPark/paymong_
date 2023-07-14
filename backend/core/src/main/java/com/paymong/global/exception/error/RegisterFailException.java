package com.paymong.global.exception.error;

import com.paymong.global.code.ErrorCode;

public class RegisterFailException extends BasicErrorException {
    public RegisterFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
