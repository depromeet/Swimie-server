package com.depromeet.memory.dto.response;

import com.depromeet.image.dto.response.MemoryImagesDto;
import java.util.List;
import lombok.Builder;

public record TimelineResponseDto(
        Long memoryId,
        String recordAt,
        String startTime,
        String endTime,
        Short lane,
        String diary,
        Integer totalMeter,
        Long memoryDetailId,
        String item,
        Short heartRate,
        String pace,
        Integer kcal,
        List<StrokeResponse> strokes,
        List<MemoryImagesDto> images) {
    @Builder
    public TimelineResponseDto {}
}

/*
* ## 📌 Description
- 조회에 필요한 컬럼
  - 달력 미리보기 이미지(이건 프론트에서 가져와야 함) , recordAt(날짜, 요일), startTime, endTime
  - 총거리(stroke 별 거리 계산 후 추가)
    - 만약 laps 가 있고 거리 없으면 laps * lane으로 계산
    - meter 면 그냥 그거 가져오면 됨
  - 영법 별 거리 (stroke meter or laps * memory lane)
     - 만약 laps 가 있고 거리 없으면 laps * lane으로 계산
     - meter 면 그냥 그거 가져오면 됨
  - image, memoryDetail.kcal, memory.diary
- 조회 방식 : 무한 스크롤 방식(cursor 기반 스크롤)
* */
