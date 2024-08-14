package com.depromeet.image.domain.vo;

import lombok.Builder;

public record ImagePresignedUrlNameVo(
        Long imageId, String originImageName, String imageName, String presignedUrl) {
    @Builder
    public ImagePresignedUrlNameVo {}
}
