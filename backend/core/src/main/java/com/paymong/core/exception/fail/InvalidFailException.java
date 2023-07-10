package com.paymong.core.exception.fail;

import com.paymong.core.code.BasicFailCode;

public class InvalidFailException extends BasicFailException {
    public InvalidFailException(BasicFailCode failCode) { super(failCode); }
}
