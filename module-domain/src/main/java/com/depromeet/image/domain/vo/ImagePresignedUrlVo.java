package com.depromeet.image.domain.vo;

import lombok.Builder;

public record ImagePresignedUrlVo(Long imageId, String imageName, String presignedUrl) {
    @Builder
    public ImagePresignedUrlVo {}
}
