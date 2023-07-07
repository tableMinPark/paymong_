package com.paymong.core.response;

import com.paymong.core.code.BasicFailCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FailResponse extends BasicResponse {
    private final FailResponseData data;

    public FailResponse(BasicFailCode failCode) {
        super("fail");
        this.data = FailResponseData.builder()
                .title(failCode.getTitle())
                .content(failCode.getContent())
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