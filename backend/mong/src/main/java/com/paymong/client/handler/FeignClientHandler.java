package com.paymong.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.global.response.BasicResponse;
import com.paymong.global.response.ErrorResponse;
import com.paymong.global.response.FailResponse;
import com.paymong.global.code.FeignClientCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class FeignClientHandler {
    private static ObjectMapper objectMapper;

    @Autowired
    public FeignClientHandler(ObjectMapper objectMapper) {
        FeignClientHandler.objectMapper = objectMapper;
    }

    public static FeignClientCode getCode(FeignException e) throws RuntimeException {
         try {
             BasicResponse basicResponse = objectMapper.readValue(e.contentUTF8(), BasicResponse.class);
             log.info("FeignClientHandler : {} : {}", basicResponse, basicResponse.getStatus());

             if (FeignClientCode.FAIL.status.equals(basicResponse.getStatus())) {
                 FailResponse failResponse = objectMapper.readValue(e.contentUTF8(), FailResponse.class);
                 log.info("FeignClientHandler : {} : {} : {}", failResponse.getStatus(), failResponse.getData().getTitle(), failResponse.getData().getContent());
                 return FeignClientCode.FAIL;
             } else if (FeignClientCode.ERROR.status.equals(basicResponse.getStatus())) {
                 ErrorResponse errorResponse = objectMapper.readValue(e.contentUTF8(), ErrorResponse.class);
                 log.info("FeignClientHandler : {} : {} : {}", errorResponse.getStatus(), errorResponse.getMessage(), errorResponse.getCode());
                 return FeignClientCode.ERROR;
             } else
                 return FeignClientCode.INTERNAL_SERVER;
         } catch (IOException ex) {
             ex.printStackTrace();
             throw new RuntimeException();
         }
    }
}
