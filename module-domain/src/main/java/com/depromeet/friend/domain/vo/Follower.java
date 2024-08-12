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
    //    private String profile;
    //    private String message;
    private boolean hasFollowedBack;

    @Builder
    public Follower(Long friendId, Long memberId, String name, boolean hasFollowedBack) {
        this.friendId = friendId;
        this.memberId = memberId;
        this.name = name;
        this.hasFollowedBack = hasFollowedBack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Follower follower = (Follower) o;
        return hasFollowedBack == follower.hasFollowedBack
                && friendId.equals(follower.friendId)
                && memberId.equals(follower.memberId)
                && name.equals(follower.name);
    }
}
