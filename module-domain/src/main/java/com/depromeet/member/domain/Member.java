package com.depromeet.member.domain;

import com.depromeet.member.port.in.command.UpdateMemberCommand;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {
    private Long id;
    private String nickname;
    private String email;
    private MemberRole role;
    private String providerId;
    private Integer goal;
    private MemberGender gender;
    private String profileImageUrl;
    private String introduction;
    private LocalDateTime lastViewedFollowingLogAt;

    @Builder
    public Member(
            Long id,
            String nickname,
            String email,
            MemberRole role,
            String providerId,
            Integer goal,
            MemberGender gender,
            String profileImageUrl,
            String introduction,
            LocalDateTime lastViewedFollowingLogAt) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.providerId = providerId;
        this.goal = goal;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
        this.lastViewedFollowingLogAt = lastViewedFollowingLogAt;
    }

    public Member updateGoal(Integer goal) {
        this.goal = goal;
        return this;
    }

    public Member updateNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Member updateGender(MemberGender gender) {
        this.gender = gender;
        return this;
    }

    public Member updateLastViewedFollowingLogAt() {
        this.lastViewedFollowingLogAt = LocalDateTime.now();
        return this;
    }

    public Member update(UpdateMemberCommand command) {
        this.nickname = command.nickname();
        this.introduction = command.introduction();
        return this;
    }

    public Member updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
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
