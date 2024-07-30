package com.depromeet.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "이름 수정 정보 입력")
public record NameUpdateRequest(@NotBlank String name) {}
