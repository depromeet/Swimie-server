package com.depromeet.member.domain.vo;

import java.util.List;
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

    public String getProfileImageUrl(String profileImageOrigin) {
        if (profileImageUrl == null) {
            return null;
        } else if (isDefaultProfileNumber(profileImageUrl)) {
            return profileImageUrl;
        }
        StringBuilder builder = new StringBuilder();
        return builder.append(profileImageOrigin).append("/").append(profileImageUrl).toString();
    }

    private static final List<String> DEFAULT_IMAGE_NAMES = List.of("1", "2", "3", "4");

    private static boolean isDefaultProfileNumber(String imageName) {
        return DEFAULT_IMAGE_NAMES.contains(imageName);
    }
}
