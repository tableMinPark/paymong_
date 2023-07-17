package com.paymong.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MapCode {
    NORMAL("MP000", "기본");

    public final String code;
    public final String name;
}
