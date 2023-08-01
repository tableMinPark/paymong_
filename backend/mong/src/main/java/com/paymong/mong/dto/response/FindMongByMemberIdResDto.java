package com.paymong.mong.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindMongByMemberIdResDto {
    private Long mongId;
}
