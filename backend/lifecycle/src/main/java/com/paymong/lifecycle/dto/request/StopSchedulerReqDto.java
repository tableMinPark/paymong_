package com.paymong.lifecycle.dto.request;

import com.paymong.global.code.LifeCycleCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StopSchedulerReqDto {
    private List<LifeCycleCode> code;
}
