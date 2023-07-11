package com.paymong.collect.global.code;

import com.paymong.core.code.BasicFailCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CollectFailCode implements BasicFailCode {
    DUPLICATION_MAP_CODE(HttpStatus.BAD_REQUEST, "중복된 맵 코드", "이미 등록 된 맵 코드 입니다."),
    DUPLICATION_MONG_CODE(HttpStatus.BAD_REQUEST, "중복된 몽 코드", "이미 등록 된 몽 코드 입니다."),
    INVALID_MAP_CODE(HttpStatus.BAD_REQUEST, "존재하지 않는 맵 코드", "유효한 맵 코드가 아닙니다."),
    INVALID_MONG_CODE(HttpStatus.BAD_REQUEST, "존재하지 않는 몽 코드", "유효한 몽 코드가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String content;
}
