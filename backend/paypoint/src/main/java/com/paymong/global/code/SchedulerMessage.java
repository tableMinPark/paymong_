package com.paymong.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SchedulerMessage {
    // Map
    MAP("기본 맵으로 변경"),
    MAP_ALREADY("이미 맵 스케줄러가 존재함"),
    MAP_START("맵 스케줄러 시작"),
    MAP_PAUSE("맵 스케줄러 일시중지"),
    MAP_RESTART("맵 스케줄러 재시작"),
    MAP_STOP("맵 스케줄러 중지"),
    MAP_NOT_FOUND("맵 스케줄러를 찾을 수 없음");

    public final String message;
}
