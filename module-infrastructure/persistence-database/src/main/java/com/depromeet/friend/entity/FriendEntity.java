package com.depromeet.friend.entity;

import com.depromeet.basetime.BaseTimeEntity;
import com.depromeet.friend.domain.Friend;
import com.depromeet.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendEntity extends BaseTimeEntity {
    @Id
    @Column(name = "friend_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @JoinColumn(name = "following_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity following;

    @Builder
    public FriendEntity(Long id, MemberEntity member, MemberEntity following) {
        this.id = id;
        this.member = member;
        this.following = following;
    }

    public static FriendEntity from(Friend friend) {
        return FriendEntity.builder()
                .member(MemberEntity.from(friend.getMember()))
                .following(MemberEntity.from(friend.getFollowing()))
                .build();
    }

    public Friend toModel() {
        return Friend.builder()
                .id(this.id)
                .member(this.member.toModel())
                .following(this.following.toModel())
                .createdAt(getCreatedAt())
                .build();
    }
}
