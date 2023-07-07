package com.paymong.auth.global.code;

import com.paymong.core.code.BasicFailCode;

public enum AuthFailCode implements BasicFailCode {
    NOT_FOUND_SESSION("존재하지 않는 세션", "회원과 일치하는 세션이 존재하지 않습니다."),
    NOT_FOUND_MEMBER("등록되지 않은 회원", "플레이어 아이디와 일치하는 회원 정보가 없습니다."),
    UN_AUTHENTICATION("권한 없음", "현재 권한으로는 접근할 수 없습니다."),
    EXPIRED_ACCESS_TOKEN("엑세스 토큰 만료", "액세스 토큰이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN("리프레시 토큰 만료", "리프레시 토큰이 만료되었습니다."),
    INVALID_TOKEN("토큰 형식 불일치", "유효하지 않은 토큰 형식입니다."),
    INVALID_PLAYER_ID("플레이어 아이디 형식 불일치", "유효하지 않은 플레이어 아이디 형식입니다."),
    UN_AUTHENTICATION_TOKEN("토큰 불일치", "토큰이 일치하지 않습니다.");

    private final String title;
    private final String content;

    AuthFailCode(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
