package com.paymong.core.exception.errorException;

import com.paymong.core.code.ErrorCode;

public class DeleteException extends BasicErrorException {
    public DeleteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
