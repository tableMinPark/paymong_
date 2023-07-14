package com.paymong.mong.global.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.core.exception.fail.InvalidFailException;
import com.paymong.mong.dto.request.RegisterPayPointReqDto;
import com.paymong.mong.global.client.dto.response.FindPayPointResDto;
import com.paymong.mong.global.code.FeignClientCode;
import com.paymong.mong.global.code.MongFailCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayPointService {
    private final PayPointClient payPointClient;
    private final ObjectMapper objectMapper;

    public Boolean registerPayPoint(RegisterPayPointReqDto registerPayPointReqDto) throws RuntimeException {
        try {
            FindPayPointResDto findPayPointResDto = objectMapper.convertValue(payPointClient.registerPayPoint(registerPayPointReqDto), FindPayPointResDto.class);
            Boolean data = findPayPointResDto.getData();
            return data;
        } catch (FeignException e) {
            FeignClientCode code = FeignClientHandler.getCode(e);

            if (code == FeignClientCode.FAIL)
                throw new InvalidFailException(MongFailCode.REGISTER_PAY_POINT);
            else
                throw new RuntimeException();
        }
    }
}
