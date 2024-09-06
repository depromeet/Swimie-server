package com.depromeet.blacklist.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.blacklist.domain.Blacklist;
import com.depromeet.member.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlacklistEntity extends BaseTimeEntity {
    @Id
    @Column(name = "blacklist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "black_target_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity blackMember;

    @Builder
    public BlacklistEntity(Long id, MemberEntity member, MemberEntity blackMember) {
        this.id = id;
        this.member = member;
        this.blackMember = blackMember;
    }

    public static BlacklistEntity from(Blacklist blacklist) {
        return BlacklistEntity.builder()
                .member(MemberEntity.from(blacklist.getMember()))
                .blackMember(MemberEntity.from(blacklist.getBlackMember()))
                .build();
    }

    public Blacklist toModel() {
        return Blacklist.builder()
                .id(this.id)
                .member(this.member.toModel())
                .blackMember(this.blackMember.toModel())
                .createdAt(getCreatedAt())
                .build();
    }
}
