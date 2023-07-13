package com.paymong.mong.dto.response;

import com.paymong.mong.dto.MongStatusDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindMongStatusResDto {
    private Long mongId;
    private String name;
    private Double health;
    private Double satiety;
    private Double strength;
    private Double sleep;

    public static FindMongStatusResDto of(Long mongId, String name, MongStatusDto mongStatusDto) {
        return FindMongStatusResDto.builder()
                .mongId(mongId)
                .name(name)
                .health(mongStatusDto.getHealth())
                .satiety(mongStatusDto.getSatiety())
                .strength(mongStatusDto.getStrength())
                .sleep(mongStatusDto.getSleep())
                .build();
    }
}
