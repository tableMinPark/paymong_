package com.paymong.global.exception;

import com.paymong.global.code.GatewayFailCode;

public class InvalidException extends BasicFailException {
    public InvalidException(GatewayFailCode code) {
        super(code);
    }
}
