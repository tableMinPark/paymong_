package com.paymong.paypoint.repository;

import java.time.LocalDateTime;

public interface FoodAndSnackMapping {
    String getName();
    String getFoodCode();
    Integer getPrice();
    LocalDateTime getLastBuy();
}
