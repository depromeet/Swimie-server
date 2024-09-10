package com.depromeet.friend.domain.vo;

import java.util.List;
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

    @Builder
    public Follower(
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
