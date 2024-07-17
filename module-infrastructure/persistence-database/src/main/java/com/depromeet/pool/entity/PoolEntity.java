package com.depromeet.pool.entity;

import com.depromeet.pool.Pool;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PoolEntity {
    @Id
    @Column(name = "pool_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private Integer lane;

    @Builder
    public PoolEntity(Long id, String name, String address, Integer lane) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lane = lane;
    }

    public static PoolEntity from(Pool pool) {
        return PoolEntity.builder()
                .id(pool.getId())
                .name(pool.getName())
                .address(pool.getAddress())
                .lane(pool.getLane())
                .build();
    }

    public Pool toModel() {
        return Pool.builder()
                .id(this.id)
                .name(this.name)
                .address(this.address)
                .lane(this.lane)
                .build();
    }
}
