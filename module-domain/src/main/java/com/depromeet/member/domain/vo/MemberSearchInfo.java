package com.depromeet.member.domain.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSearchInfo {
    private Long memberId;
    private String nickname;
    private String profileImageUrl;
    private String introduction;
    private boolean hasFollowed;

    @Builder
    public MemberSearchInfo(
            Long memberId,
            String nickname,
            String profileImageUrl,
            String introduction,
            boolean hasFollowed) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
        this.hasFollowed = hasFollowed;
    }
}
