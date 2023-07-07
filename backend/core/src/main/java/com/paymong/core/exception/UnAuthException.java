package com.paymong.core.exception;

import com.paymong.core.code.BasicFailCode;

public class UnAuthException extends BasicException {
    public UnAuthException(BasicFailCode basicFailCode) {
        super(basicFailCode);
    }
}
