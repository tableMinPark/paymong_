package com.paymong.global.task;

import com.paymong.global.code.MapCode;
import com.paymong.global.code.PayPointFailCode;
import com.paymong.global.code.TaskMessage;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.global.scheduler.BasicScheduler;
import com.paymong.paypoint.entity.CommonCode;
import com.paymong.paypoint.entity.MemberMap;
import com.paymong.paypoint.repository.MemberMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MapTask implements BasicTask {
    private final MemberMapRepository memberMapRepository;

    @Override
    @Transactional
    public void afterStop(Long memberId, BasicScheduler basicScheduler) throws NotFoundFailException {
        // 스케줄러 중지
        basicScheduler.stopScheduler(memberId);

        MemberMap memberMap = memberMapRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new NotFoundFailException(PayPointFailCode.NOT_FOUND_MAP));

        // 기본맵으로 변경
        memberMap.setCode(CommonCode.builder().code(MapCode.NORMAL.code).build());

        BasicTask.writeLog(memberId, TaskMessage.MAP);
    }
}
