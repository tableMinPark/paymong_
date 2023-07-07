package com.paymong.core.code;

public enum ErrorCode {
    /*
        prefix 규칙
            NOT_FOUND_ : 조회 실패
            NOT_DELETE_ : 삭제 실패
            NOT_REGISTER_ : 등록 실패
            UN_AUTHENTICATION_ : 비인가
            EXPIRED_ : 만료
            INVALID_ : 유효하지 않음
    */
    INTERNAL_SERVER("서버 내부 에러", 500),
    NOT_REGISTER_REDIS("레디스 데이터 등록 실패", 501),
    NOT_FOUND_REDIS("레디스 데이터 조회 실패", 502),
    NOT_MODIFY_REDIS("레디스 데이터 수정 실패", 503),
    NOT_DELETE_REDIS("레디스 데이터 삭제 실패", 504);

    private final String message;
    private final Integer code;

    ErrorCode(final String message, final Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getCode() {
        return this.code;
    }

}
