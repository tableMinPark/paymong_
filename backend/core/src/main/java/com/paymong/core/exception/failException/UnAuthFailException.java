package com.paymong.core.exception.failException;

import com.paymong.core.code.BasicFailCode;

public class UnAuthFailException extends BasicFailException {
    public UnAuthFailException(BasicFailCode basicFailCode) {
        super(basicFailCode);
    }
}
