package com.paymong.mong.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMongReqDto {
    private String name;
    private LocalTime sleepStart;
    private LocalTime sleepEnd;;
}
