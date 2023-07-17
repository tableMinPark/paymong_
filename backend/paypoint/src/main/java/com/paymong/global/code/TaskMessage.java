package com.paymong.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TaskMessage {
    DEATH("몽 사망"),
    EVOLUTION("몽 진화 대기 상태로 돌입"),
    HEALTH("몽 체력 감소"),
    MAP("기본 맵으로 변경"),
    INVALID_HEALTH("몽이 체력 감소할 수 없는 상태입니다."),
    SICK("몽 아픈 상태로 돌입");

    public String message;
}
