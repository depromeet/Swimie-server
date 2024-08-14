package com.depromeet.friend.domain.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Follower {
    private Long friendId;
    private Long memberId;
    private String name;
    private String profileImageUrl;
    private String introduction;
    private boolean hasFollowedBack;

    @Builder
    public Follower(
            Long friendId,
            Long memberId,
            String name,
            String profileImageUrl,
            String introduction,
            boolean hasFollowedBack) {
        this.friendId = friendId;
        this.memberId = memberId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
        this.hasFollowedBack = hasFollowedBack;
    }
}
