package com.paymong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongConditionCode {
    NORMAL("CD000"),
    SICK("CD001"),
    SLEEP("CD002"),
    SOMNOLENCE("CD003"),
    HUNGRY("CD004"),
    DIE("CD005"),
    GRADUATE("CD006"),
    EVOLUTION_READY("CD007"),
    EATING("CD008");

    public final String code;
}
