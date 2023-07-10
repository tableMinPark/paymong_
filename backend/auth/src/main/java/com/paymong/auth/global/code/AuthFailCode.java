package com.paymong.auth.global.code;

import com.paymong.core.code.BasicFailCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthFailCode implements BasicFailCode {

    /*
        prefix 규칙
        NOT_FOUND_ : 조회실패
        UN_AUTHENTICATION_ : 비인가
        EXPIRED_ : 만료
        INVALID_ : 유효하지 않음
    */

    // 로그인
    INVALID_PLAYER_ID(HttpStatus.UNAUTHORIZED, "플레이어 아이디 형식 불일치", "유효하지 않은 플레이어 아이디 형식입니다."),
    // 토큰 재발급
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 없음", "리프레시 토큰이 없습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰 만료", "리프레시 토큰이 만료되었습니다."),
    INVALID_TOKEN_FORM(HttpStatus.UNAUTHORIZED, "토큰 형식 불일치", "유효하지 않은 토큰 형식입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰", "유효하지 않은 토큰입니다."),
    UN_AUTHENTICATION_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 불일치", "회원에 등록된 토큰과 입력 토큰이 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String content;
}
