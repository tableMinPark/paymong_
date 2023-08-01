package com.paymong.global.exception.fail;

import com.paymong.global.code.BasicFailCode;

public class UnAuthFailException extends BasicFailException {
    public UnAuthFailException(BasicFailCode basicFailCode) {
        super(basicFailCode);
    }
}
