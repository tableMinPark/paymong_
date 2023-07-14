package com.paymong.mong.dto;

import com.paymong.core.exception.fail.InvalidFailException;
import com.paymong.mong.entity.Mong;
import com.paymong.mong.global.code.MongFailCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MongStatusDto {
    private Double health;
    private Double satiety;
    private Double strength;
    private Double sleep;

    public static MongStatusDto of(Mong mong) throws RuntimeException {
        int level = Integer.parseInt(mong.getMongCode().substring(2, 3));
        int tier = Integer.parseInt(mong.getMongCode().substring(3, 4));
        double decrease = 0.0;

        Double[][] decLevel = {
                {},
                {0.0, 20.0},
                {0.0, 30.0, 35.0, 40.0, 25.0},
                {0.0, 40.0, 45.0, 50.0, 35.0}
        };

        switch (level) {
            case 0:
                break;
            case 1:
                decrease = decLevel[1][0];
                break;
            case 2:
                if (tier == 1)
                    decrease = decLevel[2][1];
                else if (tier == 2)
                    decrease = decLevel[2][2];
                else if (tier == 3)
                    decrease = decLevel[2][3];
                else
                    decrease = decLevel[2][4];
                break;
            case 3:
                if (tier == 1)
                    decrease = decLevel[3][1];
                else if (tier == 2)
                    decrease = decLevel[3][2];
                else if (tier == 3)
                    decrease = decLevel[3][3];
                else
                    decrease = decLevel[3][4];
                break;
            default:
                throw new InvalidFailException(MongFailCode.INVALID_MONG_LEVEL);
        }

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
