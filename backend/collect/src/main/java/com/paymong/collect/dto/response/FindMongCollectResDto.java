package com.paymong.collect.dto.response;

import com.paymong.collect.repository.MongCollectMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class FindMongCollectResDto {
    List<MongCollectVo> eggs;
    List<MongCollectVo> level1;
    List<MongCollectVo> level2;
    List<MongCollectVo> level3;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MongCollectVo {
        private Boolean isOpen;
        private String name;
        private String characterCode;

        public static MongCollectVo of(MongCollectMapping mongCollectMapping) {
            return MongCollectVo.builder()
                    .isOpen(mongCollectMapping.getMemberId() != null)
                    .characterCode(mongCollectMapping.getCode())
                    .name(mongCollectMapping.getName())
                    .build();
        }
    }

    public static FindMongCollectResDto toList(List<MongCollectMapping> mongCollectMappingList) {
        FindMongCollectResDto findMongCollectResDto = FindMongCollectResDto.builder()
                .eggs(new ArrayList<>())
                .level1(new ArrayList<>())
                .level2(new ArrayList<>())
                .level3(new ArrayList<>())
                .build();

        mongCollectMappingList.forEach(mongCollectMapping -> {
            int level = Integer.parseInt(mongCollectMapping.getCode().replace("CH", ""));
            if(level < 100)
                findMongCollectResDto.getEggs().add(MongCollectVo.of(mongCollectMapping));
            else if (level < 200)
                findMongCollectResDto.getLevel1().add(MongCollectVo.of(mongCollectMapping));
            else if (level < 300)
                findMongCollectResDto.getLevel2().add(MongCollectVo.of(mongCollectMapping));
            else if (level < 400)
                findMongCollectResDto.getLevel3().add(MongCollectVo.of(mongCollectMapping));
        });

        return findMongCollectResDto;
    }
}
