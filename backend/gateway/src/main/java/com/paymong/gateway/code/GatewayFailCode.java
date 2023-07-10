package com.paymong.gateway.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GatewayFailCode {

    /*
        prefix 규칙
        NOT_FOUND_ : 조회실패
        UN_AUTHENTICATION_ : 비인가
        EXPIRED_ : 만료
        INVALID_ : 유효하지 않음
    */
    NOT_FOUND_ROLES(HttpStatus.UNAUTHORIZED, "권한 확인 불가", "사용자의 권한을 확인할 수 없습니다."),
    UN_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증 불가", "회원 인증을 할 수 없습니다."),
    // Spring Security
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 없음", "엑세스 토큰이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰", "유효하지 않은 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "엑세스 토큰 만료", "액세스 토큰이 만료되었습니다."),
    UN_AUTHENTICATION_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 불일치", "회원에 등록된 토큰과 입력 토큰이 일치하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String title;
    private final String content;
}
