package com.paymong.mong.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MongActiveCode {

    /*
    'AT000','식사','AT','활동'
    'AT001','간식','AT','활동'
    'AT002','배변','AT','활동'
    'AT003','수면','AT','활동'
    'AT004','산책','AT','활동'
    'AT005','훈련','AT','활동'
    'AT006','쓰다듬기','AT','활동'
    'AT007','기상','AT','활동'
    'AT008','청소','AT','활동'
    'AT009','진화','AT','활동'
    'AT010','졸업','AT','활동'
    'AT011','패널티','AT','활동'
    'AT012','배틀','AT','활동'
    'AT013','충전','AT','활동'
    'AT014','충전 중지','AT','활동'
     */

    FOOD("AT000"),
    SNACK("AT001"),
    POOP("AT002"),
    SLEEP("AT003"),
    WALKING("AT004"),
    TRAINING("AT005"),
    STROKE("AT006"),
    WAKEUP("AT007"),
    CLEAN("AT008"),
    EVOLUTION("AT009"),
    GRADUATION("AT010"),
    PENALTY("AT011"),
    BATTLE("AT012"),
    CHARGE("AT013"),
    STOP_CHARGE("AT014");

    public String code;
}
