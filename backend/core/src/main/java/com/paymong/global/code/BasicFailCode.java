package com.paymong.global.code;

import org.springframework.http.HttpStatus;

public interface BasicFailCode {
    HttpStatus getHttpStatus();
    String getTitle();
    String getContent();
}