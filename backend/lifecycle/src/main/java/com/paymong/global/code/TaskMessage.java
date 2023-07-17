package com.paymong.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TaskMessage {

    DEATH("몽 죽음"),
    EVOLUTION("몽 진화 대기 상태로 돌입"),
    HEALTH("몽 체력감소"),
    POOP("몽 똥생성"),
    SATIETY("몽 포만감감소"),
    SLEEP("몽 수면"),
    STROKE("몽 쓰다듬기 가능해짐"),

    INVALID_HEALTH("몽이 체력 감소할 수 없는 상태입니다."),
    INVALID_POOP("몽이 똥 생성할 수 없는 상태입니다."),
    SICK("몽 아픈 상태로 돌입"),
    HUNGRY("몽 배고픔 상태로 돌입");

    public final String message;
}
