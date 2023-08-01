package com.paymong.global.exception.fail;

import com.paymong.global.code.BasicFailCode;

public class InvalidFailException extends BasicFailException {
    public InvalidFailException(BasicFailCode failCode) { super(failCode); }
}
