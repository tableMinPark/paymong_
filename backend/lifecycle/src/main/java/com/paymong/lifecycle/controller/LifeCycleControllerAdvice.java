package com.paymong.lifecycle.controller;

import com.paymong.global.code.BasicFailCode;
import com.paymong.global.code.ErrorCode;
import com.paymong.global.code.FailCode;
import com.paymong.global.exception.fail.InvalidFailException;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.response.ErrorResponse;
import com.paymong.global.response.FailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class LifeCycleControllerAdvice {
    @ExceptionHandler({ InvalidFailException.class })
    private ResponseEntity<Object> handleInvalidFailException(InvalidFailException e) {
        BasicFailCode failCode = e.getFailCode();
        log.info("handleInvalidFailException : {} : {}", failCode.getTitle(), failCode.getContent());
        return ResponseEntity.status(failCode.getHttpStatus()).body(new FailResponse(failCode));
    }

    @ExceptionHandler({ NotFoundFailException.class })
    private ResponseEntity<Object> handleNotFoundFailException(NotFoundFailException e) {
        BasicFailCode failCode = e.getFailCode();
        log.info("handleNotFoundFailException : {} : {}", failCode.getTitle(), failCode.getContent());
        return ResponseEntity.status(failCode.getHttpStatus()).body(new FailResponse(failCode));
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    private ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        BasicFailCode failCode = FailCode.INVALID_ARGS;
        log.info("handleHttpMessageNotReadableException : {} : {}", failCode.getTitle(), failCode.getContent());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FailResponse(failCode));
    }

    @ExceptionHandler({ RuntimeException.class })
    private ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        log.info("handleRuntimeException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ErrorCode.INTERNAL_SERVER));
    }

    @ExceptionHandler({ NoHandlerFoundException.class })
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.info("handleNoHandlerFoundException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new FailResponse(FailCode.NOT_FOUND));
    }
}
