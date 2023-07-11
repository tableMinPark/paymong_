package com.paymong.core.exception.error;

import com.paymong.core.code.ErrorCode;
import com.paymong.core.exception.fail.BasicFailException;

public class RegisterFailException extends BasicErrorException {
    public RegisterFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
