package com.paymong.auth.global.code;

import com.paymong.core.code.BasicFailCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    NOT_FOUND_TOKEN("토큰이 없음", "리프레시 토큰이 없습니다."),
    NOT_FOUND_SESSION("존재하지 않는 세션", "회원과 일치하는 세션이 존재하지 않습니다."),
    NOT_FOUND_MEMBER("등록되지 않은 회원", "플레이어 아이디와 일치하는 회원 정보가 없습니다."),
    UN_AUTHENTICATION("권한 없음", "현재 권한으로는 접근할 수 없습니다."),
    EXPIRED_ACCESS_TOKEN("엑세스 토큰 만료", "액세스 토큰이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN("리프레시 토큰 만료", "리프레시 토큰이 만료되었습니다."),
    INVALID_TOKEN("유효하지 않은 토큰", "유효하지 않은 토큰입니다."),
    INVALID_TOKEN_FORM("토큰 형식 불일치", "유효하지 않은 토큰 형식입니다."),
    INVALID_PLAYER_ID("플레이어 아이디 형식 불일치", "유효하지 않은 플레이어 아이디 형식입니다."),
    UN_AUTHENTICATION_TOKEN("토큰 불일치", "회원에 등록된 토큰과 입력 토큰이 일치하지 않습니다.");

    private final String title;
    private final String content;
}
