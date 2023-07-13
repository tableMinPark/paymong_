package com.paymong.collect.dto.response;

import com.paymong.collect.repository.MapCollectMapping;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FindMapCollectResDto {
    private Boolean isOpen;
    private String name;
    private String mapCode;

    public static List<FindMapCollectResDto> toList(List<MapCollectMapping> mapCollectMappings) {
        return mapCollectMappings.stream()
                .map(FindMapCollectResDto::of)
                .collect(Collectors.toList());
    }

    public static FindMapCollectResDto of(MapCollectMapping mapCollectMapping) {
        return FindMapCollectResDto.builder()
                .isOpen(mapCollectMapping.getMemberId() != null)
                .name(mapCollectMapping.getName())
                .mapCode(mapCollectMapping.getCode())
                .build();
    }
}
