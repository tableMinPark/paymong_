package com.paymong.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
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

    INTERNAL_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러", 500),
    NOT_REGISTER(HttpStatus.INTERNAL_SERVER_ERROR,"데이터 등록 오류", 501),
    NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,"데이터 조회 오류", 502),
    NOT_MODIFY(HttpStatus.INTERNAL_SERVER_ERROR,"데이터 수정 오류", 503),
    NOT_DELETE(HttpStatus.INTERNAL_SERVER_ERROR,"데이터 삭제 오류", 504);

    private final HttpStatus httpStatus;
    private final String message;
    private final Integer code;
}
