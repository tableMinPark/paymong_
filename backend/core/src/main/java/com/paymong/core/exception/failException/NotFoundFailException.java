package com.paymong.core.exception.failException;

import com.paymong.core.code.BasicFailCode;

public class NotFoundFailException extends BasicFailException {
    public NotFoundFailException(BasicFailCode basicFailCode) {
        super(basicFailCode);
    }
}
