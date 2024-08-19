package com.depromeet.image.dto.response;

import com.depromeet.image.domain.vo.ImagePresignedUrlNameVo;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProfileImageUploadResponse(
        @Schema(
                        description = "변환된 이미지 UUID 이름",
                        example = "30c448d-8807.jpg",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String imageName,
        @Schema(description = "이미지 업로드할 Presigned URL", requiredMode = Schema.RequiredMode.REQUIRED)
                String presignedUrl) {
    public static ProfileImageUploadResponse of(ImagePresignedUrlNameVo imagePresignedUrlNameVo) {
        return new ProfileImageUploadResponse(
                imagePresignedUrlNameVo.imageName(), imagePresignedUrlNameVo.presignedUrl());
    }
}
