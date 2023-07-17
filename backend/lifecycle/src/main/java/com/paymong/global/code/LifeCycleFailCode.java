package com.paymong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LifeCycleFailCode implements BasicFailCode {

    /*
        prefix 규칙
        NOT_FOUND_ : 조회실패
        UN_AUTHENTICATION_ : 비인가
        EXPIRED_ : 만료
        INVALID_ : 유효하지 않음
    */

    NOT_FOUND_MONG(HttpStatus.BAD_REQUEST, "존재하지 않는 몽", "해당 아이디와 일치하는 몽이 없습니다."),
    INVALID_SERVICE(HttpStatus.BAD_REQUEST, "지원되지 않는 기능", "해당 스케줄러는 지원되지 않는 기능입니다."),
    INVALID_LIFECYCLE_CODE(HttpStatus.BAD_REQUEST, "잘못된 라이프 사이클 코드", "입력 된 라이프 사이클 코드가 적절하지 않습니다."),
    INVALID_STATUS_CODE(HttpStatus.BAD_REQUEST, "이미 적용 된 스케줄러", "이미 적용되어 있기 때문에 스케줄러를 가동할 필요가 없습니다.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String content;
}
