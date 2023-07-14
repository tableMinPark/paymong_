package com.paymong.paypoint.service;

import com.paymong.global.exception.fail.InvalidFailException;
import com.paymong.global.exception.fail.NotFoundFailException;
import com.paymong.paypoint.dto.request.RegisterPayPointReqDto;
import com.paymong.paypoint.dto.response.FindFoodAndSnack;
import com.paymong.paypoint.dto.response.FindPayPointInfoResDto;
import com.paymong.paypoint.dto.response.FindPayPointResDto;
import com.paymong.paypoint.entity.PayPoint;
import com.paymong.paypoint.entity.PayPointHistory;
import com.paymong.global.code.PayPointFailCode;
import com.paymong.global.security.CustomUserDetail;
import com.paymong.paypoint.repository.CommonCodeRepository;
import com.paymong.paypoint.repository.FoodAndSnackMapping;
import com.paymong.paypoint.repository.PayPointHistoryRepository;
import com.paymong.paypoint.repository.PayPointRepository;
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
    private final CommonCodeRepository commonCodeRepository;

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
    public List<FindFoodAndSnack> findFoodAndSnack(String foodCategory) throws RuntimeException {
        Long memberId = getMemberId();

        List<FoodAndSnackMapping> foodAndSnackMappingList = commonCodeRepository.findFoodAndSnack(memberId, foodCategory);

        return FindFoodAndSnack.toList(foodAndSnackMappingList);
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
