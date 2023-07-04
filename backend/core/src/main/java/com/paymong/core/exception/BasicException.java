package com.paymong.core.exception;

import com.paymong.core.code.BasicFailCode;

public class BasicException extends RuntimeException {
    private BasicFailCode failCode;

    public BasicException(BasicFailCode basicFailCode){
        this.failCode = basicFailCode;
    }

    public BasicFailCode getFailCode() {
        return this.failCode;
    }
}
