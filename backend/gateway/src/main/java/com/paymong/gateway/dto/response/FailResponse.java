package com.paymong.gateway.dto.response;

import com.paymong.global.code.GatewayFailCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FailResponse extends BasicResponse {
    private final FailResponseData data;

    public FailResponse(GatewayFailCode gatewayFailCode) {
        super("fail");
        this.data = FailResponseData.builder()
                .title(gatewayFailCode.getTitle())
                .content(gatewayFailCode.getContent())
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FailResponseData {
        private String title;
        private String content;
    }
}