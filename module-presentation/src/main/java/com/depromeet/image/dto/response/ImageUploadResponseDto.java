package com.depromeet.image.dto.response;

import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImageUploadResponseDto(Long imageId, String imageName, String presignedUrl) {
    @Builder
    public ImageUploadResponseDto {}

    public static ImageUploadResponseDto of(ImagePresignedUrlVo imagePresignedUrlVo) {
        return ImageUploadResponseDto.builder()
                .imageId(imagePresignedUrlVo.imageId())
                .imageName(imagePresignedUrlVo.imageName())
                .presignedUrl(imagePresignedUrlVo.presignedUrl())
                .build();
    }
}
