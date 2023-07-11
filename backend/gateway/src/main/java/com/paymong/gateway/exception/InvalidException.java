package com.paymong.gateway.exception;

import com.paymong.gateway.code.GatewayFailCode;

public class InvalidException extends BasicFailException {
    public InvalidException(GatewayFailCode code) {
        super(code);
    }
}
