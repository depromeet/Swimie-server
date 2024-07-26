package com.depromeet.memory;

import com.depromeet.image.Image;
import com.depromeet.member.Member;
import com.depromeet.pool.domain.Pool;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Memory {
    private Long id;
    private Member member;
    private Pool pool;
    private MemoryDetail memoryDetail;
    private List<Stroke> strokes;
    private List<Image> images;
    private LocalDate recordAt;
    private LocalTime startTime;
    private LocalTime endTime;
    private Short lane;
    private String diary;

    @Builder
    public Memory(
            Long id,
            Member member,
            Pool pool,
            MemoryDetail memoryDetail,
            List<Stroke> strokes,
            List<Image> images,
            LocalDate recordAt,
            LocalTime startTime,
            LocalTime endTime,
            Short lane,
            String diary) {
        this.id = id;
        this.member = member;
        this.pool = pool;
        this.memoryDetail = memoryDetail;
        this.strokes = strokes;
        this.images = images;
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lane = lane;
        this.diary = diary;
    }

    public void setStrokes(List<Stroke> strokes) {
        if (strokes != null && !strokes.isEmpty()) {
            this.strokes = strokes;
        }
    }

    public Memory update(Memory updateMemory) {
        return Memory.builder()
                .id(id)
                .member(member)
                .pool(updateMemory.getPool())
                .memoryDetail(updateMemory.getMemoryDetail())
                .strokes(updateMemory.getStrokes())
                .images(images)
                .recordAt(updateMemory.getRecordAt())
                .startTime(updateMemory.getStartTime())
                .endTime(updateMemory.getEndTime())
                .lane(updateMemory.getLane())
                .diary(updateMemory.getDiary())
                .build();
    }
}
