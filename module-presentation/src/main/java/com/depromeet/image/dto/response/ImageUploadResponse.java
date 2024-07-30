package com.depromeet.image.dto.response;

import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageUploadResponse(Long imageId, String imageName, String presignedUrl) {
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
