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

    REGISTER_PAY_POINT(HttpStatus.BAD_REQUEST, "페이 포인트 등록 실패", "페이 포인트를 등록하는 과정에서 오류가 발생했습니다."),
    INVALID_INVALID_REQUEST_DTO(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값", "입력된 값이 유효하지 않습니다."),
    REGISTER_MONG(HttpStatus.BAD_REQUEST, "몽이 존재함", "생성한 몽이 존재합니다."),
    INVALID_TRAINING_COUNT(HttpStatus.BAD_REQUEST, "유효하지 않은 훈련 횟수", "훈련 횟수가 너무 적습니다."),
    INVALID_WALKING_COUNT(HttpStatus.BAD_REQUEST, "유효하지 않은 산책 횟수", "산책 횟수가 너무 적습니다."),
    INVALID_MONG_LEVEL(HttpStatus.BAD_REQUEST, "유효하지 않은 레벨", "유효하지 않은 몽 레벨 정보 입니다."),
    NOT_FOUND_ACTIVE(HttpStatus.BAD_REQUEST, "존재하지 않는 활동", "해당 활동 코드와 일치하는 활동이 없습니다."),
    NOT_FOUND_MONG(HttpStatus.BAD_REQUEST, "존재하지 않는 몽", "해당 아이디와 일치하는 몽이 없습니다."),
    NOT_FOUND_FOOD(HttpStatus.BAD_REQUEST, "존재하지 않는 음식", "해당 음식 코드와 일치하는 음식이 없습니다."),
    NOT_FOUND_SNACK(HttpStatus.BAD_REQUEST, "존재하지 않는 간식", "해당 간식 코드와 일치하는 간식이 없습니다.");

    private final HttpStatus httpStatus;
    private final String title;
    private final String content;
}
