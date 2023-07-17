package com.paymong.client.dto.request;

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
public class RestartSchedulerReqDto {
    private List<LifeCycleCode> code;
}
