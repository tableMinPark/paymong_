package com.paymong.core.response;

import lombok.Builder;

@Builder
public class BasicResponse {
    String status;

    public BasicResponse(String status) {
        this.status = status;
    }
}



