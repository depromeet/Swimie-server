package com.depromeet.blacklist.dto.response;

import com.depromeet.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlackMemberDetailResponse(
        @Schema(
                        description = "차단한 유저 아이디",
                        example = "2",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long memberId,
        @Schema(
                        description = "차단한 유저 닉네임",
                        example = "스탑제로",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String nickname,
        @Schema(
                        description = "차단한 유저 프로필 이미지",
                        example = "https://example.com/image.jpg OR 기본 이미지(1,2,3,4)",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String profileImageUrl,
        @Schema(
                        description = "차단한 유저 소개",
                        example = "안녕하세요",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String introduction) {
    public static List<BlackMemberDetailResponse> of(List<Member> blackMembers, String domain) {
        return blackMembers.stream()
                .map(
                        member ->
                                new BlackMemberDetailResponse(
                                        member.getId(),
                                        member.getNickname(),
                                        getProfileImageUrl(member, domain),
                                        member.getIntroduction()))
                .toList();
    }

    private static String getProfileImageUrl(Member member, String domain) {
        if (member.getProfileImageUrl() == null) {
            return null;
        }
        if (isDefaultImage(member)) {
            return member.getProfileImageUrl();
        }
        return domain + "/" + member.getProfileImageUrl();
    }

    private static boolean isDefaultImage(Member member) {
        String url = member.getProfileImageUrl();
        return url.equals("1") || url.equals("2") || url.equals("3") || url.equals("4");
    }
}
