package com.paymong.mong.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyEvolutionResDto {
    private String mongCode;
    private String stateCode;
}
