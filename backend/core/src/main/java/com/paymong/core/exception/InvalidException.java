package com.paymong.core.exception;

import com.paymong.core.code.BasicFailCode;

public class InvalidException extends BasicException {
    public InvalidException(BasicFailCode basicFailCode) {
        super(basicFailCode);
    }
}
