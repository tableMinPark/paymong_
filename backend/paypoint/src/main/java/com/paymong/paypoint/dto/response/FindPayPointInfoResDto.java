package com.paymong.paypoint.dto.response;

import com.paymong.paypoint.entity.PayPointHistory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FindPayPointInfoResDto {
    private Long pointHistoryId;
    private Long memberId;
    private Integer point;
    private String action;
    private LocalDateTime regDt;

    public static FindPayPointInfoResDto of(PayPointHistory payPointHistory) {
        return FindPayPointInfoResDto.builder()
                .pointHistoryId(payPointHistory.getPayPointHistoryId())
                .memberId(payPointHistory.getMemberId())
                .point(payPointHistory.getPoint())
                .action(payPointHistory.getComment())
                .regDt(payPointHistory.getRegDt())
                .build();
    }

    public static List<FindPayPointInfoResDto> toList(List<PayPointHistory> payPointHistoryList) {
        return payPointHistoryList.stream()
                .map(FindPayPointInfoResDto::of)
                .collect(Collectors.toList());
    }
}
