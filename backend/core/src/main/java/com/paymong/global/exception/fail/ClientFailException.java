package com.paymong.global.exception.fail;

import com.paymong.global.code.BasicFailCode;

public class ClientFailException extends BasicFailException {
    public ClientFailException(BasicFailCode failCode) { super(failCode); }
}
