package com.paymong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FailCode implements BasicFailCode {

    /*
        prefix 규칙
        NOT_FOUND_ : 조회실패
        UN_AUTHENTICATION_ : 비인가
        EXPIRED_ : 만료
        INVALID_ : 유효하지 않음
    */

    // Basic
    INVALID_ARGS(HttpStatus.BAD_REQUEST, "잘못된 요청", "잘못된 입력 값입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 요청", "잘못된 URI 요청입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한 없음", "현재 권한으로는 접근할 수 없습니다."),
    UN_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증 불가", "회원 인증을 할 수 없습니다."),
    // Spring Security
    NOT_FOUND_MEMBER(HttpStatus.UNAUTHORIZED, "등록되지 않은 회원", "플레이어 아이디와 일치하는 회원 정보가 없습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "엑세스 토큰 만료", "액세스 토큰이 만료되었습니다."),
    UN_AUTHENTICATION_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 불일치", "회원에 등록된 토큰과 입력 토큰이 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String content;
}
