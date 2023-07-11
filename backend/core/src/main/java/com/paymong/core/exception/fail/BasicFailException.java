package com.paymong.core.exception.fail;

import com.paymong.core.code.BasicFailCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BasicFailException extends RuntimeException {
    protected final BasicFailCode code;

    public BasicFailCode getFailCode() {
        return this.code;
    }
}
