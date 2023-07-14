package com.paymong.global.exception.error;

import com.paymong.global.code.ErrorCode;

public class DeleteFailException extends BasicErrorException {
    public DeleteFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
