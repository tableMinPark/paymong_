package com.paymong.gateway.exception;

import com.paymong.gateway.code.GatewayFailCode;

public class HeaderException extends BasicFailException {
    public HeaderException(GatewayFailCode code) {
        super(code);
    }
}
