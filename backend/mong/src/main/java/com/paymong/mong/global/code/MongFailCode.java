package com.paymong.mong.global.code;

import com.paymong.core.code.BasicFailCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MongFailCode implements BasicFailCode {

    /*
        prefix 규칙
        NOT_FOUND_ : 조회실패
        UN_AUTHENTICATION_ : 비인가
        EXPIRED_ : 만료
        INVALID_ : 유효하지 않음
    */
    INVALID_TIME(HttpStatus.BAD_REQUEST, "유효하지 않은 시간", "입력된 시간이 유효하지 않습니다."),
    REGISTER_MONG(HttpStatus.BAD_REQUEST, "몽이 존재함", "생성한 몽이 존재합니다."),
    INVALID_MONG_LEVEL(HttpStatus.BAD_REQUEST, "유효하지 않은 레벨", "유효하지 않은 몽 레벨 정보 입니다."),
    NOT_FOUND_MONG(HttpStatus.BAD_REQUEST, "존재하지 않는 몽", "해당 아이디와 일치하는 몽이 없습니다.");


    private final HttpStatus httpStatus;
    private final String title;
    private final String content;
}
