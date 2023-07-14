package com.paymong.mong.dto.response;

import com.paymong.mong.dto.common.MongStatusDto;
import com.paymong.mong.entity.Mong;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyWalkingResDto {
    private String code;
    private Integer poopCount;
    private String message;
    private String stateCode;
    private Integer weight;
    private Double health;
    private Double satiety;
    private Double strength;
    private Double sleep;

    public static ModifyWalkingResDto of(Mong mong, MongStatusDto mongStatusDto) {
        return ModifyWalkingResDto.builder()
                .code(mong.getMongCode())
                .poopCount(mong.getPoopCount())
                .message(mong.getStatusCode())
                .stateCode(mong.getStatusCode())
                .weight(mong.getWeight())
                .health(mongStatusDto.getHealth())
                .satiety(mongStatusDto.getSatiety())
                .strength(mongStatusDto.getStrength())
                .sleep(mongStatusDto.getSleep())
                .build();
    }
}
