package com.paymong.gateway.exception;

import com.paymong.gateway.code.GatewayFailCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BasicFailException extends RuntimeException {
    protected final GatewayFailCode code;
    public GatewayFailCode getFailCode() {
        return this.code;
    }
}
