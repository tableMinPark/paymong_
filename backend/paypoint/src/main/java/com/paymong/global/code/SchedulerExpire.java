package com.paymong.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SchedulerExpire {
    MAP_EXPIRE(5L, "MAP");

    public final Long expire;
    public final String message;
}
