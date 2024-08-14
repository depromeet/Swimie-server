package com.depromeet.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberUpdateRequest(
        @NotBlank(message = "멤버의 이름은 비어 있을 수 없습니다") String nickname, String introduction) {
    public MemberUpdateRequest {}
}
