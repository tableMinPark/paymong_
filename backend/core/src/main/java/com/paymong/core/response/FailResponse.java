package com.paymong.core.response;

import com.paymong.core.code.BasicFailCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FailResponse extends BasicResponse {
    FailResponseData data;

    public FailResponse(BasicFailCode basicFailCode){
        super("fail");
        this.data = FailResponseData.builder()
                .title(basicFailCode.getTitle())
                .content(basicFailCode.getContent())
                .build();
    }

    @Builder
    private static class FailResponseData {
        private String title;
        private String content;
    }
}

