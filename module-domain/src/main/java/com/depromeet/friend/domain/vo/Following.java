package com.depromeet.friend.domain.vo;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class Following {
    private Long friendId;
    private Long memberId;
    private String name;
    private String profileImageUrl;
    private String introduction;

    @Builder
    public Following(
            Long friendId,
            Long memberId,
            String name,
            String profileImageUrl,
            String introduction) {
        this.friendId = friendId;
        this.memberId = memberId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
    }
}
