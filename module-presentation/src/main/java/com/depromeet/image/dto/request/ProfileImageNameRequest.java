package com.depromeet.image.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProfileImageNameRequest(
        @Schema(
                        description = "UUID 로 변환된 이미지 이름",
                        example = "ABCD1234.png",
                        requiredMode = Schema.RequiredMode.NOT_REQUIRED)
                String imageName) {}
