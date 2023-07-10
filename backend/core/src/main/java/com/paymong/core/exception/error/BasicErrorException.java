package com.paymong.core.exception.error;

import com.paymong.core.code.BasicFailCode;
import com.paymong.core.code.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BasicErrorException extends RuntimeException {
    protected final ErrorCode code;

    public ErrorCode getFailCode() {
        return this.code;
    }
}
