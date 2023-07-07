package com.paymong.auth.auth.controller;

import com.paymong.core.code.BasicFailCode;
import com.paymong.core.code.ErrorCode;
import com.paymong.core.exception.errorException.DeleteException;
import com.paymong.core.exception.errorException.RegisterException;
import com.paymong.core.exception.failException.InvalidFailException;
import com.paymong.core.exception.failException.NotFoundFailException;
import com.paymong.core.exception.failException.UnAuthFailException;
import com.paymong.core.response.ErrorResponse;
import com.paymong.core.response.FailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler({ InvalidFailException.class })
    private ResponseEntity<Object> handleInvalidFailException(InvalidFailException e) {
        BasicFailCode failCode = e.getFailCode();
        log.error("handleInvalidFailException : {} : {}", failCode.getTitle(), failCode.getContent());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponse(failCode));
    }

    @ExceptionHandler({ NotFoundFailException.class })
    private ResponseEntity<Object> handleNotFoundFailException(NotFoundFailException e) {
        BasicFailCode failCode = e.getFailCode();
        log.error("handleNotFoundFailException : {} : {}", failCode.getTitle(), failCode.getContent());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailResponse(failCode));
    }

    @ExceptionHandler({ UnAuthFailException.class })
    private ResponseEntity<Object> handleUnAuthFailException(UnAuthFailException e) {
        BasicFailCode failCode = e.getFailCode();
        log.error("handleUnAuthFailException : {} : {}", failCode.getTitle(), failCode.getContent());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new FailResponse(failCode));
    }

    @ExceptionHandler({ RegisterException.class })
    private ResponseEntity<Object> handleRegisterException(RegisterException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("handleRegisterException : {} : {}", errorCode.getMessage(), errorCode.getCode());
        return ResponseEntity.internalServerError().body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler({ DeleteException.class })
    private ResponseEntity<Object> handleDeleteException(DeleteException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("handleDeleteException : {} : {}", errorCode.getMessage(), errorCode.getCode());
        return ResponseEntity.internalServerError().body(new ErrorResponse(errorCode));
    }

    @ExceptionHandler({ RuntimeException.class })
    private ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        log.error("handleRuntimeException : {} :", e.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponse(ErrorCode.INTERNAL_SERVER));
    }
}
