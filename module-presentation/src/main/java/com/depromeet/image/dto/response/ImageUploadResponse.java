package com.depromeet.image.dto.response;

import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageUploadResponse(
        @Schema(
                        description = "image PK",
                        example = "1",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                Long imageId,
        @Schema(
                        description = "image name",
                        example = "image.png",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String imageName,
        @Schema(
                        description = "image presignedUrl",
                        example = "https://presigned-url.png",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                String presignedUrl) {
    @Builder
    public ImageUploadResponse {}

    public static ImageUploadResponse of(ImagePresignedUrlVo imagePresignedUrlVo) {
        return ImageUploadResponse.builder()
                .imageId(imagePresignedUrlVo.imageId())
                .imageName(imagePresignedUrlVo.imageName())
                .presignedUrl(imagePresignedUrlVo.presignedUrl())
                .build();
    }
}
