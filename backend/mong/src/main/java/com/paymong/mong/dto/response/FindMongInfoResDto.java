package com.paymong.mong.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FindMongInfoResDto {
    private String name;
    private Integer weight;
    private LocalDateTime born;
}
