package com.paymong.global.exception;

import com.paymong.global.code.GatewayFailCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BasicFailException extends RuntimeException {
    protected final GatewayFailCode code;
    public GatewayFailCode getFailCode() {
        return this.code;
    }
}
