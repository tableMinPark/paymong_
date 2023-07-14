package com.paymong.global.response;

import com.paymong.global.code.BasicFailCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FailResponse extends BasicResponse {
    private FailResponseData data;

    public FailResponse(FailResponseData data) {
        this.data = data;
    }

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