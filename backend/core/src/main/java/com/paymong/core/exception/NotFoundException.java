package com.paymong.core.exception;

import com.paymong.core.code.BasicFailCode;

public class NotFoundException extends BasicException {
    public NotFoundException(BasicFailCode basicFailCode) {
        super(basicFailCode);
    }
}
