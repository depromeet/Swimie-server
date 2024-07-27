package com.depromeet.pool.entity;

import com.depromeet.member.entity.MemberEntity;
import com.depromeet.pool.domain.PoolSearch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PoolSearchEntity {
    @Id
    @Column(name = "pool_search_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @NotNull
    @JoinColumn(name = "pool_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PoolEntity pool;

    @Builder
    public PoolSearchEntity(Long id, MemberEntity member, PoolEntity pool) {
        this.id = id;
        this.member = member;
        this.pool = pool;
    }

    public static PoolSearchEntity from(PoolSearch poolSearch) {
        return PoolSearchEntity.builder()
                .id(poolSearch.getId())
                .member(MemberEntity.from(poolSearch.getMember()))
                .pool(PoolEntity.from(poolSearch.getPool()))
                .build();
    }

    public PoolSearch toModel() {
        return PoolSearch.builder()
                .id(this.id)
                .member(this.member.toModel())
                .pool(this.pool.toModel())
                .build();
    }
}
