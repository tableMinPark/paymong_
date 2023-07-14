package com.paymong.core.exception.fail;

import com.paymong.core.code.BasicFailCode;

public class ClientFailException extends BasicFailException {
    public ClientFailException(BasicFailCode failCode) { super(failCode); }
}
