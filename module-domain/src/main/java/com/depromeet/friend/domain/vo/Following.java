package com.depromeet.friend.domain.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Following {
    private Long friendId;
    private Long memberId;
    private String name;

    //    private String profile;
    //    private String message;

    @Builder
    public Following(Long friendId, Long memberId, String name) {
        this.friendId = friendId;
        this.memberId = memberId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Following following = (Following) o;
        return friendId.equals(following.friendId)
                && memberId.equals(following.memberId)
                && name.equals(following.name);
    }
}
