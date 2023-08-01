package com.paymong.mong.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindMongResDto {
    private Long mongId;
    private String name;
    private String mapCode;
    private String mongCode;
    private String stateCode;
    private Integer poopCount;
}
