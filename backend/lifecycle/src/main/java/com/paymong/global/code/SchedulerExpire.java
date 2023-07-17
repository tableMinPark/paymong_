package com.paymong.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SchedulerExpire {
    DEATH_EXPIRE(60L * 60L * 3L, "DEATH"),
    EVOLUTION_EXPIRE_LV1(60L * 10L, "EVOLUTION_LEVEL_1"),
    EVOLUTION_EXPIRE_LV2(60L * 60L * 36L, "EVOLUTION_LEVEL_2"),
    EVOLUTION_EXPIRE_LV3(60L * 60L * 24L, "EVOLUTION_LEVEL_3"),
    SLEEP_EXPIRE(30L * 60L, "SLEEP"),
    STROKE_EXPIRE(5L, "STROKE");

    public final Long expire;
    public final String message;
}
