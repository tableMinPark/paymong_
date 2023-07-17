package com.paymong.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymong.client.dto.request.*;
import com.paymong.client.dto.response.FindPayPointResDto;
import com.paymong.client.handler.FeignClientHandler;
import com.paymong.client.repository.LifeCycleClient;
import com.paymong.global.code.FeignClientCode;
import com.paymong.global.code.MongFailCode;
import com.paymong.global.exception.fail.InvalidFailException;
import com.paymong.global.response.BasicResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LifeCycleService {
    private final LifeCycleClient lifeCycleClient;
    private final ObjectMapper objectMapper;

    public Boolean startScheduler(StartSchedulerReqDto startSchedulerReqDto) throws RuntimeException {
        try {
            BasicResponse basicResponse = objectMapper.convertValue(lifeCycleClient.startScheduler(startSchedulerReqDto).getBody(), BasicResponse.class);
            return basicResponse.getStatus().equals(FeignClientCode.SUCCESS.status);
        } catch (FeignException e) {
            FeignClientCode code = FeignClientHandler.getCode(e);

            if (code == FeignClientCode.FAIL)
                throw new InvalidFailException(MongFailCode.CLIENT_START_SCHEDULER);
            else
                throw new RuntimeException();
        }
    }

    public Boolean pauseScheduler(PauseSchedulerReqDto pauseSchedulerReqDto) throws RuntimeException {
        try {
            BasicResponse basicResponse = objectMapper.convertValue(lifeCycleClient.pauseScheduler(pauseSchedulerReqDto).getBody(), BasicResponse.class);
            return basicResponse.getStatus().equals(FeignClientCode.SUCCESS.status);
        } catch (FeignException e) {
            FeignClientCode code = FeignClientHandler.getCode(e);

            if (code == FeignClientCode.FAIL)
                throw new InvalidFailException(MongFailCode.CLIENT_PAUSE_SCHEDULER);
            else
                throw new RuntimeException();
        }
    }

    public Boolean restartScheduler(RestartSchedulerReqDto restartSchedulerReqDto) throws RuntimeException {
        try {
            BasicResponse basicResponse = objectMapper.convertValue(lifeCycleClient.restartScheduler(restartSchedulerReqDto).getBody(), BasicResponse.class);
            return basicResponse.getStatus().equals(FeignClientCode.SUCCESS.status);
        } catch (FeignException e) {
            FeignClientCode code = FeignClientHandler.getCode(e);

            if (code == FeignClientCode.FAIL)
                throw new InvalidFailException(MongFailCode.CLIENT_RESTART_SCHEDULER);
            else
                throw new RuntimeException();
        }
    }

    public Boolean stopScheduler(StopSchedulerReqDto stopSchedulerReqDto) throws RuntimeException {
        try {
            BasicResponse basicResponse = objectMapper.convertValue(lifeCycleClient.stopScheduler(stopSchedulerReqDto).getBody(), BasicResponse.class);
            return basicResponse.getStatus().equals(FeignClientCode.SUCCESS.status);
        } catch (FeignException e) {
            FeignClientCode code = FeignClientHandler.getCode(e);

            if (code == FeignClientCode.FAIL)
                throw new InvalidFailException(MongFailCode.CLIENT_STOP_SCHEDULER);
            else
                throw new RuntimeException();
        }
    }

    public Boolean registerMongScheduler(RegisterMongSchedulerReqDto registerMongSchedulerReqDto) throws RuntimeException {
        try {
            BasicResponse basicResponse = objectMapper.convertValue(lifeCycleClient.registerMongScheduler(registerMongSchedulerReqDto).getBody(), BasicResponse.class);
            return basicResponse.getStatus().equals(FeignClientCode.SUCCESS.status);
        } catch (FeignException e) {
            FeignClientCode code = FeignClientHandler.getCode(e);

            if (code == FeignClientCode.FAIL)
                throw new InvalidFailException(MongFailCode.CLIENT_START_SCHEDULER);
            else
                throw new RuntimeException();
        }
    }

    public Boolean nextLevelScheduler(NextLevelSchedulerReqDto nextLevelSchedulerReqDto) throws RuntimeException {
        try {
            BasicResponse basicResponse = objectMapper.convertValue(lifeCycleClient.nextLevelScheduler(nextLevelSchedulerReqDto).getBody(), BasicResponse.class);
            return basicResponse.getStatus().equals(FeignClientCode.SUCCESS.status);
        } catch (FeignException e) {
            FeignClientCode code = FeignClientHandler.getCode(e);

            if (code == FeignClientCode.FAIL)
                throw new InvalidFailException(MongFailCode.CLIENT_EVOLUTION_SCHEDULER);
            else
                throw new RuntimeException();
        }
    }
}
