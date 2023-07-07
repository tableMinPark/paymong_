package com.paymong.core.exception.failException;

import com.paymong.core.code.BasicFailCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasicFailException extends RuntimeException {
    private BasicFailCode failCode;
}
