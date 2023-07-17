package com.paymong.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SchedulerInterval {
    HEALTH_INTERVAL(30L * 60L, "HEALTH"),
    POOP_INTERVAL(0L, "POOP"),
    SATIETY_INTERVAL(15L * 60L, "SATIETY"),
    DEEP_SLEEP_INTERVAL(0L, "DEEP_SLEEP");


    public final Long interval;
    public final String message;
}
