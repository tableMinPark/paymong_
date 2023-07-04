package com.paymong.core.exception;

import com.paymong.core.code.BasicFailCode;

public class RegisterException extends BasicException{
    public RegisterException(BasicFailCode basicFailCode) {
        super(basicFailCode);
    }
}
