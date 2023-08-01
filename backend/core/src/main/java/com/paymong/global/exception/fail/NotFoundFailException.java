package com.paymong.global.exception.fail;

import com.paymong.global.code.BasicFailCode;

public class NotFoundFailException extends BasicFailException {
    public NotFoundFailException(BasicFailCode basicFailCode) {
        super(basicFailCode);
    }
}
