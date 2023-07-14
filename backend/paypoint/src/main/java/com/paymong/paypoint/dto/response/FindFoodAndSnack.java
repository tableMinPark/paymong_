package com.paymong.paypoint.dto.response;

import com.paymong.paypoint.repository.FoodAndSnackMapping;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FindFoodAndSnack {
    private String name;
    private String foodCode;
    private Integer price;
    private LocalDateTime lastBuy;

    public static FindFoodAndSnack of(FoodAndSnackMapping foodAndSnackMapping) {
        return FindFoodAndSnack.builder()
                .name(foodAndSnackMapping.getName())
                .foodCode(foodAndSnackMapping.getFoodCode())
                .price(foodAndSnackMapping.getPrice())
                .lastBuy(foodAndSnackMapping.getLastBuy())
                .build();
    }

    public static List<FindFoodAndSnack> toList(List<FoodAndSnackMapping> foodAndSnackMappingList) {
        return foodAndSnackMappingList.stream()
                .map(FindFoodAndSnack::of)
                .collect(Collectors.toList());
    }
}
