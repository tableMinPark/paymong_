package com.paymong.paypoint.dto.response;

import com.paymong.paypoint.repository.FoodAndSnackMapping;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FindFoodAndSnackResDto {
    private String name;
    private String foodCode;
    private Integer price;
    private LocalDateTime lastBuy;

    public static FindFoodAndSnackResDto of(FoodAndSnackMapping foodAndSnackMapping) {
        return FindFoodAndSnackResDto.builder()
                .name(foodAndSnackMapping.getName())
                .foodCode(foodAndSnackMapping.getFoodCode())
                .price(foodAndSnackMapping.getPrice())
                .lastBuy(foodAndSnackMapping.getLastBuy())
                .build();
    }

    public static List<FindFoodAndSnackResDto> toList(List<FoodAndSnackMapping> foodAndSnackMappingList) {
        return foodAndSnackMappingList.stream()
                .map(FindFoodAndSnackResDto::of)
                .collect(Collectors.toList());
    }
}
