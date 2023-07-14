package com.paymong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PayPointFailCode implements BasicFailCode {

    /*
        prefix 규칙
        NOT_FOUND_ : 조회실패
        UN_AUTHENTICATION_ : 비인가
        EXPIRED_ : 만료
        INVALID_ : 유효하지 않음
    */
    INVALID_REQUEST_DTO(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값", "입력된 입력값이 유효하지 않습니다."),
    INVALID_CONTENT(HttpStatus.BAD_REQUEST, "유효하지 않은 결제 내역", "결제 내역 입력값이 유효하지 않습니다."),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "포인트 잔액 부족", "소비하기 위한 포인트 잔액이 없습니다."),
    NOT_FOUND_PAY_POINT(HttpStatus.BAD_REQUEST, "페이 포인트 정보가 없음", "회원과 일치하는 잔여 페이 포인트 정보가 없습니다.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String content;
}
