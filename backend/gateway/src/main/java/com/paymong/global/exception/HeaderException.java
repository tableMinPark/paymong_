package com.paymong.global.exception;

import com.paymong.global.code.GatewayFailCode;

public class HeaderException extends BasicFailException {
    public HeaderException(GatewayFailCode code) {
        super(code);
    }
}
