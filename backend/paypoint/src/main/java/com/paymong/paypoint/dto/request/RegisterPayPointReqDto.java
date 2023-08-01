package com.paymong.paypoint.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPayPointReqDto {
    private String content;
    private Integer price;
    private String code = "PY000";
}
