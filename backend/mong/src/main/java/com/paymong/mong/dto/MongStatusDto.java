package com.paymong.mong.dto;

import com.paymong.mong.entity.Mong;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MongStatusDto {
    private Double health;
    private Double satiety;
    private Double strength;
    private Double sleep;

    public static MongStatusDto of(Mong mong, Double decrease) {
        double health = mong.getHealth().doubleValue() / decrease;
        double satiety = mong.getSatiety().doubleValue() / decrease;
        double strength = mong.getStrength().doubleValue() / decrease;
        double sleep = mong.getSleep().doubleValue() / decrease;

        if (decrease > 0.0) {
            health /= decrease;
            satiety /= decrease;
            strength /= decrease;
            sleep /= decrease;
        }

        return MongStatusDto.builder()
                .health(Math.round(health * 100.0) / 100.0)
                .satiety(Math.round(satiety * 100.0) / 100.0)
                .strength(Math.round(strength * 100.0) / 100.0)
                .sleep(Math.round(sleep * 100.0) / 100.0)
                .build();
    }
}
