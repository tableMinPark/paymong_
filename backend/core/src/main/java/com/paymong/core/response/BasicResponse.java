package com.paymong.core.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasicResponse {
    String status;

    public BasicResponse(String status) {
        this.status = status;
    }
}



