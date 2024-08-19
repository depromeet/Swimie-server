package com.depromeet.image.domain.vo;

import lombok.Builder;

public record ImagePresignedUrlNameVo(String imageName, String presignedUrl) {
    @Builder
    public ImagePresignedUrlNameVo {}
}
