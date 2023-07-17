package com.paymong.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SchedulerMessage {
    // DEATH
    DEATH_ALREADY("이미 죽음 스케줄러가 존재함"),
    DEATH_START("죽음 스케줄러 시작"),
    DEATH_PAUSE("죽음 스케줄러 일시중지"),
    DEATH_RESTART("죽음 스케줄러 재시작"),
    DEATH_STOP("죽음 스케줄러 중지"),
    DEATH_NOT_FOUND("죽음 스케줄러를 찾을 수 없음"),

    // EVOLUTION
    EVOLUTION_ALREADY("이미 진화 스케줄러가 존재함"),
    EVOLUTION_START("진화 스케줄러 시작"),
    EVOLUTION_PAUSE("진화 스케줄러 일시중지"),
    EVOLUTION_RESTART("진화 스케줄러 재시작"),
    EVOLUTION_STOP("진화 스케줄러 중지"),
    EVOLUTION_NOT_FOUND("진화 스케줄러를 찾을 수 없음"),
    EVOLUTION_NEXT_LEVEL("새로운 진화 스케줄러를 시작"),
    EVOLUTION_NEXT_LEVEL_REGISTER("새로운 진화 스케줄러를 저장"),
    EVOLUTION_NEXT_LEVEL_DELETE("기존 진화 스케줄러를 삭제"),
    EVOLUTION_LEVEL("레벨이 적절하지 않음"),

    // HEALTH
    HEALTH_ALREADY("이미 체력감소 스케줄러가 존재함"),
    HEALTH_START("체력감소 스케줄러 시작"),
    HEALTH_STOP("체력감소 스케줄러 중지"),
    HEALTH_NOT_FOUND("체력감소 스케줄러를 찾을 수 없음"),

    // POOP
    POOP_ALREADY("이미 똥생성 스케줄러가 존재함"),
    POOP_START("똥생성 스케줄러 시작"),
    POOP_STOP("똥생성 스케줄러 중지"),
    POOP_NOT_FOUND("똥생성 스케줄러를 찾을 수 없음"),

    // SATIETY
    SATIETY_ALREADY("이미 포만감감소 스케줄러가 존재함"),
    SATIETY_START("포만감감소 스케줄러 시작"),
    SATIETY_STOP("포만감감소 스케줄러 중지"),
    SATIETY_NOT_FOUND("포만감감소 스케줄러를 찾을 수 없음"),

    // DEEP_SLEEP
    DEEP_SLEEP_ALREADY("이미 정기 수면 스케줄러가 존재함"),
    DEEP_SLEEP_START("정기 수면 스케줄러 시작"),
    DEEP_SLEEP_STOP("정기 수면 스케줄러 중지"),
    DEEP_SLEEP_NOT_FOUND("정기 수면 스케줄러를 찾을 수 없음"),
    DEEP_SLEEP_ALREADY_LOCK("정기 수면 시간을 위해 일반 수면 스케줄러를 지움"),
    DEEP_SLEEP_TIME_TO_SLEEP("수면 상태로 돌입"),
    DEEP_SLEEP_NOT_TIME_TO_SLEEP("수면 대기 상태로 돌입"),

    // SLEEP
    SLEEP_ALREADY("이미 일반 수면 스케줄러가 존재함"),
    SLEEP_START("일반수면 스케줄러 시작"),
    SLEEP_PAUSE("일반 수면 스케줄러 일시중지"),
    SLEEP_RESTART("일반 수면 스케줄러 재시작"),
    SLEEP_STOP("일반 수면 스케줄러 중지"),
    SLEEP_NOT_FOUND("일반 수면 스케줄러를 찾을 수 없음"),

    // STROKE
    STROKE_ALREADY("이미 쓰다듬기 스케줄러가 존재함"),
    STROKE_START("쓰다듬기 스케줄러 시작"),
    STROKE_PAUSE("쓰다듬기 스케줄러 일시중지"),
    STROKE_RESTART("쓰다듬기 스케줄러 재시작"),
    STROKE_STOP("쓰다듬기 스케줄러 중지"),
    STROKE_NOT_FOUND("쓰다듬기 스케줄러를 찾을 수 없음");

    public final String message;
}
