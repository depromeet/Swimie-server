package com.depromeet.memory.entity;

import com.depromeet.image.domain.Image;
import com.depromeet.image.entity.ImageEntity;
import com.depromeet.member.entity.MemberEntity;
import com.depromeet.memory.domain.Memory;
import com.depromeet.memory.domain.MemoryDetail;
import com.depromeet.memory.domain.Stroke;
import com.depromeet.pool.entity.PoolEntity;
import com.depromeet.reaction.domain.Reaction;
import com.depromeet.reaction.entity.ReactionEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "memory_entity",
        indexes = {@Index(name = "idx_memory_detail_id", columnList = "memory_detail_id")})
public class MemoryEntity {
    @Id
    @Column(name = "memory_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "pool_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PoolEntity pool;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_detail_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MemoryDetailEntity memoryDetail;

    @OneToMany(mappedBy = "memory")
    private List<StrokeEntity> strokes = new ArrayList<>();

    @OneToMany(mappedBy = "memory")
    private List<ImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "memory")
    private List<ReactionEntity> reactions = new ArrayList<>();

    @NotNull private LocalDate recordAt;

    @NotNull private LocalTime startTime;

    @NotNull private LocalTime endTime;

    private Short lane;

    @Column(columnDefinition = "TEXT")
    private String diary;

    @Builder
    public MemoryEntity(
            Long id,
            MemberEntity member,
            PoolEntity pool,
            MemoryDetailEntity memoryDetail,
            List<StrokeEntity> strokes,
            List<ImageEntity> images,
            List<ReactionEntity> reactions,
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
        this.reactions = reactions;
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lane = lane;
        this.diary = diary;
    }

    public static MemoryEntity from(Memory memory) {
        return MemoryEntity.builder()
                .id(memory.getId())
                .member(MemberEntity.from(memory.getMember()))
                .pool(getPoolEntityOrNull(memory))
                .memoryDetail(getMemoryDetailEntityOrNull(memory))
                .strokes(getStrokeEntitiesOrNull(memory))
                .images(getImageEntitiesOrNull(memory))
                .reactions(getReactionEntitiesOrNull(memory))
                .recordAt(memory.getRecordAt())
                .startTime(memory.getStartTime())
                .endTime(memory.getEndTime())
                .lane(memory.getLane())
                .diary(memory.getDiary())
                .build();
    }

    private static PoolEntity getPoolEntityOrNull(Memory memory) {
        return memory.getPool() != null ? PoolEntity.from(memory.getPool()) : null;
    }

    private static MemoryDetailEntity getMemoryDetailEntityOrNull(Memory memory) {
        return memory.getMemoryDetail() != null
                ? MemoryDetailEntity.from(memory.getMemoryDetail())
                : null;
    }

    private static List<StrokeEntity> getStrokeEntitiesOrNull(Memory memory) {
        return memory.getStrokes() != null
                ? memory.getStrokes().stream().map(StrokeEntity::pureFrom).toList()
                : null;
    }

    private static List<ImageEntity> getImageEntitiesOrNull(Memory memory) {
        return memory.getImages() != null
                ? memory.getImages().stream().map(ImageEntity::pureFrom).toList()
                : null;
    }

    private static List<ReactionEntity> getReactionEntitiesOrNull(Memory memory) {
        return memory.getImages() != null
                ? memory.getReactions().stream().map(ReactionEntity::pureFrom).toList()
                : null;
    }

    public Memory toModel() {
        return Memory.builder()
                .id(this.id)
                .member(this.member.toModel())
                .pool(this.pool != null ? this.pool.toModel() : null)
                .memoryDetail(getMemoryDetailOrNull())
                .strokes(getStrokeListOrNull())
                .images(getImageListOrNull())
                .reactions(getReactionOrNull())
                .recordAt(this.recordAt)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .lane(this.lane)
                .diary(this.diary)
                .build();
    }

    public Memory toModelForFollowingLog() {
        return Memory.builder()
                .id(this.id)
                .member(this.member.toModel())
                .memoryDetail(getMemoryDetailOrNull())
                .strokes(getStrokeListOrNull())
                .images(getImageListOrNull())
                .recordAt(this.recordAt)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .lane(this.lane)
                .diary(this.diary)
                .build();
    }

    public Memory toModelWithMemberOnly() {
        return Memory.builder()
                .id(this.id)
                .member(this.member.toModel())
                .recordAt(this.recordAt)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .lane(this.lane)
                .diary(this.diary)
                .build();
    }

    public Memory toModelWithMemoryDetailOnly() {
        return Memory.builder()
                .id(this.id)
                .memoryDetail(getMemoryDetailOrNull())
                .recordAt(this.recordAt)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .lane(this.lane)
                .diary(this.diary)
                .build();
    }

    private List<Image> getImageListOrNull() {
        return this.images != null
                ? this.images.stream().map(ImageEntity::pureToModel).toList()
                : null;
    }

    private List<Stroke> getStrokeListOrNull() {
        return this.strokes != null
                ? this.strokes.stream().map(StrokeEntity::pureToModel).toList()
                : null;
    }

    private List<Reaction> getReactionOrNull() {
        return this.reactions != null
                ? this.reactions.stream().map(ReactionEntity::pureToModel).toList()
                : null;
    }

    private MemoryDetail getMemoryDetailOrNull() {
        return this.memoryDetail != null ? this.memoryDetail.toModel() : null;
    }

    public String updateDiary(String diary) {
        return this.diary = diary;
    }

    public MemoryEntity update(MemoryEntity me) {
        if (me.getPool() != null) this.pool = me.getPool();
        if (me.getStrokes() != null) this.strokes = me.getStrokes();
        if (me.getImages() != null) this.images = me.getImages();
        if (me.getRecordAt() != null) this.recordAt = me.getRecordAt();
        if (me.getStartTime() != null) this.startTime = me.getStartTime();
        if (me.getEndTime() != null) this.endTime = me.getEndTime();
        if (me.getLane() != null) this.lane = me.getLane();
        this.memoryDetail = me.getMemoryDetail();
        this.diary = me.getDiary();
        return this;
    }
}
