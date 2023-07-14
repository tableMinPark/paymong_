package com.paymong.pay_point.service;

import com.paymong.core.exception.fail.InvalidFailException;
import com.paymong.core.exception.fail.NotFoundFailException;
import com.paymong.pay_point.dto.request.RegisterPayPointReqDto;
import com.paymong.pay_point.dto.response.FindPayPointInfoResDto;
import com.paymong.pay_point.dto.response.FindPayPointResDto;
import com.paymong.pay_point.entity.PayPoint;
import com.paymong.pay_point.entity.PayPointHistory;
import com.paymong.pay_point.global.code.PayPointFailCode;
import com.paymong.pay_point.global.security.CustomUserDetail;
import com.paymong.pay_point.repository.PayPointHistoryRepository;
import com.paymong.pay_point.repository.PayPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayPointService {
    private final PayPointRepository payPointRepository;
    private final PayPointHistoryRepository payPointHistoryRepository;

    @Transactional
    public FindPayPointResDto findPayPoint() throws RuntimeException {
        Long memberId = getMemberId();

        PayPoint payPoint = payPointRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundFailException(PayPointFailCode.NOT_FOUND_PAY_POINT));

        return FindPayPointResDto.builder()
                .point(payPoint.getPoint())
                .build();
    }

    @Transactional
    public List<FindPayPointInfoResDto> findPayPointInfo() throws RuntimeException {
        Long memberId = getMemberId();

        List<PayPointHistory> payPointHistoryList = payPointHistoryRepository.findByMemberIdOrderByRegDtDesc(memberId);

        return FindPayPointInfoResDto.toList(payPointHistoryList);
    }

    @Transactional
    public void registerPayPoint(RegisterPayPointReqDto registerPayPointReqDto) throws RuntimeException {
        Long memberId = getMemberId();
        String code = registerPayPointReqDto.getCode();
        Integer price = registerPayPointReqDto.getPrice();

        PayPoint payPoint = payPointRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundFailException(PayPointFailCode.NOT_FOUND_PAY_POINT));

        // 포인트 잔액 확인해서 잔액 부족이면 예외 발생
        if (payPoint.getPoint() + price < 0)
            throw new InvalidFailException(PayPointFailCode.INVALID_PRICE);

        payPoint.setPoint(payPoint.getPoint() + price);

        payPointHistoryRepository.save(PayPointHistory.builder()
                .memberId(memberId)
                .code(code)
                .comment(registerPayPointReqDto.getContent())
                .point(price)
                .build());
    }

    private Long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        String memberId = customUserDetail.getMemberId();
        return Long.parseLong(memberId);
    }
}
