package com.paymong.core.exception.error;

import com.paymong.core.code.ErrorCode;
import com.paymong.core.exception.fail.BasicFailException;

public class DeleteFailException extends BasicErrorException {
    public DeleteFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
